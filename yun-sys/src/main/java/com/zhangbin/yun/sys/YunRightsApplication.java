package com.zhangbin.yun.sys;

import com.zhangbin.yun.common.web.websocket.EnableWebSocket;
import com.zhangbin.yun.common.web.websocket.MsgType;
import com.zhangbin.yun.common.web.websocket.SocketMsg;
import com.zhangbin.yun.common.web.websocket.WebsocketSender;
import com.zhangbin.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

//@EnableWebSocket(EnableWebSocket.WebSocketType.Netty)
//@EnableWebSocket
@RestController
@Api(hidden = true)
@SpringBootApplication
public class YunRightsApplication/* implements CommandLineRunner*/ {

    public static void main(String[] args) {
        SpringApplication.run(YunRightsApplication.class, args);
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Yun Backend service started successfully";
    }

//    @Override
//    public void run(String... args) throws Exception {
//        int i = 0;
//        while (true) {
//            try { TimeUnit.SECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
//            WebsocketSender.sendMsg("", new SocketMsg("hello world -- " + i++, MsgType.INFO));
//        }
//    }
}
