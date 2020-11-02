package com.zhangbin.yun.common.web.websocket;

import com.zhangbin.yun.common.autoconfigure.EnableWebSocket;
import com.zhangbin.yun.common.web.websocket.netty.WebsocketServer;
import com.zhangbin.yun.common.web.websocket.tomcat.WebsocketClient;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

public class WebsocketSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableWebSocket.class.getName()));
        final Enum<?> anEnum = annotationAttributes.getEnum("value");
        if(EnableWebSocket.WebSocketType.Tomcat == anEnum){
            return new String[]{ServerEndpointExporter.class.getName(), WebsocketClient.class.getName()};
        }else if(EnableWebSocket.WebSocketType.Netty == anEnum){
            return new String[]{WebsocketServer.class.getName()};
        }
        return new String[0];
    }
}
