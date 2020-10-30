package com.zhangbin.yun.common.autoconfigure;

import com.zhangbin.yun.common.websocket.WebSocketSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(WebSocketSelector.class)
public @interface EnableWebSocket {

    /**
     * 开启 tomcat 或 netty 的形式的 WebSocket他
     * @return WebSocketType，默认 WebSocketType.Tomcat
     */
    WebSocketType value() default WebSocketType.Tomcat;

    enum WebSocketType {
        Tomcat, Netty
    }
}

