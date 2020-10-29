package com.zhangbin.yun.common.websocket.tomcat;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/webSocket/{sid}")
public class WebSocketClient {
    public WebSocketClient() {
        System.out.println("000");
    }

    /**
     * 存放每个客户端对应的 WebSocket 对象。
     */
    private static final CopyOnWriteArraySet<WebSocketClient> WEB_SOCKET_CLIENTS = new CopyOnWriteArraySet<WebSocketClient>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        WEB_SOCKET_CLIENTS.add(this);
        this.sid = sid;
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WEB_SOCKET_CLIENTS.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来" + sid + "的信息:" + message);
        //群发消息
        for (WebSocketClient item : WEB_SOCKET_CLIENTS) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Websocket 发生错误，错误会话：{}", session);
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(SocketMsg socketMsg, @PathParam("sid") String sid) throws IOException {
        String message = JSONObject.toJSONString(socketMsg);
        log.info("推送消息到" + sid + "，推送内容:" + message);
        for (WebSocketClient item : WEB_SOCKET_CLIENTS) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        WebSocketClient other = (WebSocketClient) o;
        return Objects.equals(sid, other.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid);
    }
}
