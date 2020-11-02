package com.zhangbin.yun.common.web.websocket;

public interface Sender {
    /**
     * 向客户端发送消息
     * @param sid 客户端 ID 标识 (全局唯一)， 若 sid 为空， 则广播消息至所有客户端
     * @param socketMsg 将要发送的消息
     */
    void sendMsg(String sid, SocketMsg socketMsg);
}
