package com.zhangbin.yun.common.web.websocket.netty;

import cn.hutool.core.util.ReflectUtil;
import com.zhangbin.yun.common.web.websocket.WebsocketSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.net.InetSocketAddress;

public class WebsocketServer implements ApplicationListener<ApplicationStartedEvent> {
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    @Value("${websocket.netty.port:9999}")
    private int port;

    ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    /**
     * 处理服务器关闭，并释放所有资源
     */
    void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new WebsocketChannelInitializer(group);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ChannelFuture future = this.start(new InetSocketAddress(port));
        ReflectUtil.setFieldValue(WebsocketSender.class, "sender", new TextWebsocketFrameHandler(channelGroup));
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
