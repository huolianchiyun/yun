package com.hlcy.yun.common.web.websocket.netty;

import com.alibaba.fastjson.JSONObject;
import com.hlcy.yun.common.utils.str.StringUtils;
import com.hlcy.yun.common.web.websocket.Sender;
import com.hlcy.yun.common.web.websocket.SocketMsg;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Slf4j
public class TextWebsocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> implements Sender {

    private final ChannelGroup group;

    public TextWebsocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 重写父类方法，以便处理自定义事件
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            // 如果该表示握手成功，则从该ChannelPipeline中移除HttpRequestHandler，因为将不会接收到任何HTTP消息了，缩短数量流程
            ctx.pipeline().remove(HttpRequestHandler.class);
            String identity = "";
            final Matcher matcher = HttpRequestHandler.WS_PATTERN.matcher(handshakeComplete.requestUri());
            if (matcher.find()) {
                identity = matcher.group(1);
            }
            // 将新的WebsocketChannel添加到ChannelGroup中,以便新客户端可以接收到所有的消息
            group.add(new WebsocketChannel(identity, ctx.channel()));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        if(log.isDebugEnabled() && StringUtils.isNotEmpty(msg.text())){
            log.debug("收到客户端信息: {}", msg.text());
        }
//        ctx.writeAndFlush(msg.retain());
    }

    @Override
    public void sendMsg(String sid, SocketMsg socketMsg) {
        if (group.isEmpty()) return;
        String message = JSONObject.toJSONString(socketMsg);
        log.info("推送消息到 {}，推送内容: {}", sid, message);
        final List<Channel> collect = group.stream().filter(c -> {
            WebsocketChannel channel = (WebsocketChannel) c;
            return channel.getIdentity().equals(sid);
        }).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            // 消息一定要使用TextWebSocketFrame进行封装，否则客户端接收不到消息
            collect.forEach(c -> c.writeAndFlush(new TextWebSocketFrame(message)));
            return;
        }
        // 群发
        group.writeAndFlush(new TextWebSocketFrame(message));
    }

    static class WebsocketChannel implements Channel {
        private String identity;
        private Channel delegate;

        public WebsocketChannel(String identity, Channel delegate) {
            this.identity = identity;
            this.delegate = delegate;
        }

        public String getIdentity() {
            return identity;
        }

        @Override
        public ChannelId id() {
            return delegate.id();
        }

        @Override
        public EventLoop eventLoop() {
            return delegate.eventLoop();
        }

        @Override
        public Channel parent() {
            return delegate.parent();
        }

        @Override
        public ChannelConfig config() {
            return delegate.config();
        }

        @Override
        public boolean isOpen() {
            return delegate.isOpen();
        }

        @Override
        public boolean isRegistered() {
            return delegate.isRegistered();
        }

        @Override
        public boolean isActive() {
            return delegate.isActive();
        }

        @Override
        public ChannelMetadata metadata() {
            return delegate.metadata();
        }

        @Override
        public SocketAddress localAddress() {
            return delegate.localAddress();
        }

        @Override
        public SocketAddress remoteAddress() {
            return delegate.remoteAddress();
        }

        @Override
        public ChannelFuture closeFuture() {
            return delegate.closeFuture();
        }

        @Override
        public boolean isWritable() {
            return delegate.isWritable();
        }

        @Override
        public long bytesBeforeUnwritable() {
            return delegate.bytesBeforeUnwritable();
        }

        @Override
        public long bytesBeforeWritable() {
            return delegate.bytesBeforeWritable();
        }

        @Override
        public Unsafe unsafe() {
            return delegate.unsafe();
        }

        @Override
        public ChannelPipeline pipeline() {
            return delegate.pipeline();
        }

        @Override
        public ByteBufAllocator alloc() {
            return delegate.alloc();
        }

        @Override
        public ChannelFuture bind(SocketAddress localAddress) {
            return delegate.bind(localAddress);
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress) {
            return delegate.connect(remoteAddress);
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
            return delegate.connect(remoteAddress, localAddress);
        }

        @Override
        public ChannelFuture disconnect() {
            return delegate.disconnect();
        }

        @Override
        public ChannelFuture close() {
            return delegate.close();
        }

        @Override
        public ChannelFuture deregister() {
            return delegate.deregister();
        }

        @Override
        public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
            return delegate.bind(localAddress, promise);
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
            return delegate.connect(remoteAddress, promise);
        }

        @Override
        public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            return delegate.connect(remoteAddress, localAddress, promise);
        }

        @Override
        public ChannelFuture disconnect(ChannelPromise promise) {
            return delegate.disconnect(promise);
        }

        @Override
        public ChannelFuture close(ChannelPromise promise) {
            return delegate.close(promise);
        }

        @Override
        public ChannelFuture deregister(ChannelPromise promise) {
            return delegate.deregister(promise);
        }

        @Override
        public Channel read() {
            return delegate.read();
        }

        @Override
        public ChannelFuture write(Object msg) {
            return delegate.write(msg);
        }

        @Override
        public ChannelFuture write(Object msg, ChannelPromise promise) {
            return delegate.writeAndFlush(msg, promise);
        }

        @Override
        public Channel flush() {
            return delegate.flush();
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
            return delegate.writeAndFlush(msg, promise);
        }

        @Override
        public ChannelFuture writeAndFlush(Object msg) {
            return delegate.writeAndFlush(msg);
        }

        @Override
        public ChannelPromise newPromise() {
            return delegate.newPromise();
        }

        @Override
        public ChannelProgressivePromise newProgressivePromise() {
            return delegate.newProgressivePromise();
        }

        @Override
        public ChannelFuture newSucceededFuture() {
            return delegate.newSucceededFuture();
        }

        @Override
        public ChannelFuture newFailedFuture(Throwable cause) {
            return delegate.newFailedFuture(cause);
        }

        @Override
        public ChannelPromise voidPromise() {
            return delegate.voidPromise();
        }

        @Override
        public <T> Attribute<T> attr(AttributeKey<T> key) {
            return delegate.attr(key);
        }

        @Override
        public <T> boolean hasAttr(AttributeKey<T> key) {
            return delegate.hasAttr(key);
        }

        @Override
        public int compareTo(Channel o) {
            return delegate.compareTo(o);
        }
    }
}
