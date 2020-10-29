package com.zhangbin.yun.common.autoconfigure;


import com.zhangbin.yun.common.websocket.tomcat.WebSocketClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(WebSocketClient.class) //TODO 可以控制开启tomcat的还是netty的
public @interface EnableWebSocket {
}
