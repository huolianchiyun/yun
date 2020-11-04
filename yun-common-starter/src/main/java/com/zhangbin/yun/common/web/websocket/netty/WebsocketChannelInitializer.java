package com.zhangbin.yun.common.web.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import static com.zhangbin.yun.common.web.websocket.WebsocketConst.WEBSOCKET_BASE_URL;

public class WebsocketChannelInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    WebsocketChannelInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 将所有需要的ChannelHandler添加到ChannelPipeline中
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new HttpRequestHandler());
    }
}
