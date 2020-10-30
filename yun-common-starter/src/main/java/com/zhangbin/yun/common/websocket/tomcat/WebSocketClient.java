package com.zhangbin.yun.common.websocket.tomcat;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket客户端连接
 * 每来一个新连接会该连接创建一个 WebSocketClient
 *
 * @see org.apache.tomcat.websocket.pojo.PojoEndpointServer#onOpen
 * @see org.springframework.web.socket.server.standard.ServerEndpointExporter
 */
@Slf4j
@ServerEndpoint("/webSocket/{sid}")
public class WebSocketClient {

    private static final AtomicInteger onlineCount = new AtomicInteger();
    /**
     * 存放每个客户端对应的 WebSocket 对象。
     */
    private static final Map<String, WebSocketClient> WEB_SOCKET_CLIENT_MAP = new ConcurrentHashMap<>(5);

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 sid
     */
    private String sid = "";

    /**
     * 服务器向客户端发送消息
     *
     * @param sid       若 sid 为 null 或空，则广播，即群发
     * @param socketMsg 自定义消息
     */
    public static void sendMsg(@PathParam("sid") String sid, SocketMsg socketMsg) {
        if (WEB_SOCKET_CLIENT_MAP.isEmpty()) return;
        String message = JSONObject.toJSONString(socketMsg);
        log.info("推送消息到 {}，推送内容: {}", sid, message);
        final WebSocketClient client = WEB_SOCKET_CLIENT_MAP.get(sid);
        if (client != null) {
            client.sendMessage(message);
            return;
        }
        // 群发
        WEB_SOCKET_CLIENT_MAP.values().parallelStream()
                .forEach(c -> c.sendMessage(message));
    }

    /**
     * 获取在线连接数
     *
     * @return onlineCount
     */
    public static int getOnlineCount() {
        return onlineCount.intValue();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        WEB_SOCKET_CLIENT_MAP.put(sid, this);
        this.sid = sid;
        onlineCount.incrementAndGet();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WEB_SOCKET_CLIENT_MAP.remove(this.sid);
        onlineCount.decrementAndGet();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来" + sid + "的信息: " + message);
        //群发消息
        for (Map.Entry<String, WebSocketClient> item : WEB_SOCKET_CLIENT_MAP.entrySet()) {
            item.getValue().sendMessage(message);
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
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("推送消息到 {}，推送内容: {}", sid, message);
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
