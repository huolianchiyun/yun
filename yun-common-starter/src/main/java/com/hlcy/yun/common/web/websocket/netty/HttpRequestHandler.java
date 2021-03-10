package com.hlcy.yun.common.web.websocket.netty;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.hlcy.yun.common.spring.SpringContextHolder;
import com.hlcy.yun.common.web.websocket.WebsocketConst;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Pattern;

@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final File INDEX_PAGE;
    private static final Pattern STATIC_RESOURCE_PATTERN = Pattern.compile("/websocket/.+\\.js");
    static final Pattern WS_PATTERN = Pattern.compile(WebsocketConst.WEBSOCKET_BASE_URL + "/(.+)");

    static {
        INDEX_PAGE = getStaticResource("websocket/index.html");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (WS_PATTERN.matcher(msg.uri()).matches()) {
            prepareWebsocketHandshake(ctx, msg);
        } else {
            handleHttp(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    private static File getStaticResource(String url) {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + url;
            path = !path.contains("file:") ? path : path.substring(5);
            return new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    /**
     * websocket handshake 预处理， 如：token校验
     *
     * @param ctx
     * @param msg
     * @throws UnsupportedEncodingException
     */
    private void prepareWebsocketHandshake(ChannelHandlerContext ctx, FullHttpRequest msg) throws UnsupportedEncodingException {
        final String token = URLDecoder.decode(msg.headers().get("Sec-WebSocket-Protocol"), "UTF-8");
        // 调用权限接口校验token， 若有权限token校验，则校验通过再ws连接，若没有权限token校验，则直接建立ws连接
        final WebsocketServer websocketServer = SpringContextHolder.getBean(WebsocketServer.class);
        final int webPort = websocketServer.getWebPort();
        final cn.hutool.http.HttpResponse response = cn.hutool.http.HttpUtil.createGet("http://localhost:" + webPort + "/yun/auth/info")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .setConnectionTimeout(10000)
                .execute();
        if (response.getStatus() == HttpStatus.HTTP_UNAUTHORIZED) {
            log.warn("*** websocket handshake failed, cause 401 No Authorization. ***");
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
        } else if (response.getStatus() == HttpStatus.HTTP_OK
                && JSON.parseObject(response.body()).getJSONObject("meta").getIntValue("status") == HttpStatus.HTTP_OK) {
            final ChannelPipeline pipeline = ctx.pipeline();
            pipeline.addLast(new WebSocketServerProtocolHandler(WebSocketServerProtocolConfig.newBuilder()
                    .websocketPath(WebsocketConst.WEBSOCKET_BASE_URL)
                    .checkStartsWith(true)
                    .subprotocols(msg.headers().get("Sec-WebSocket-Protocol"))
                    .build()));
            pipeline.addLast(new TextWebsocketFrameHandler(websocketServer.getChannelGroup()));
            ctx.fireChannelRead(msg.retain());
        }
    }

    private void handleHttp(ChannelHandlerContext ctx, FullHttpRequest msg) throws IOException {
        if (HttpUtil.is100ContinueExpected(msg)) {
            // 处理 100 Continue请求以符合HTTP 1.1规范
            // 如果客户端发送 HTTP 1.1 的HTTP头信息 Expect： 100-continue，那么HttpRequestHandler将会发送一个 100 continue响应
            send100Continue(ctx);
        }
        RandomAccessFile file;
        String CONTENT_TYPE = "text/html; charset=UTF-8";
        if (STATIC_RESOURCE_PATTERN.matcher(msg.uri()).matches()) {
            file = new RandomAccessFile(getStaticResource(msg.uri()), "r");
            CONTENT_TYPE = "text/javascript; charset=UTF-8";
        } else {
            file = new RandomAccessFile(INDEX_PAGE, "r");
        }
        HttpResponse response = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        if (keepAlive) {
            // 如果请求了 keep-alive，则添加所需的HTTP头信息
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        // 判断需不需要加密，通过ChannelPipeline中是否存在SslHandler判断
        // 如果无需加密和压缩，那么可以通过将index.html内存存储在DefaultFileRegion中来达到最佳效率，这将利用零拷贝技术进行内容传输。否则，可以使用ChunkedNioFile
        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }
        // 写LastHttpContent并冲刷至客户端
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            // 如果没有请求keep-alive，则在写操作完成后关闭Channel
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
