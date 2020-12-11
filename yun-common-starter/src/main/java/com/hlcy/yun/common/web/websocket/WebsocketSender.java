package com.hlcy.yun.common.web.websocket;

/**
 *  websocket 消息发送器
 * 向客户端发送 websocket 消息
 *
 * 当使用 @EnableWebSocket 开启 websocket功能时，程序启动过程中，会自动初始化 Sender sender
 *
 */
public final class WebsocketSender {
    private static Sender sender;

    public static void sendMsg(String sid, SocketMsg socketMsg) {
        sender.sendMsg(sid, socketMsg);
    }
}
