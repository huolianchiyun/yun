package com.hlcy.yun.admincenter.listener;

import com.hlcy.yun.common.web.websocket.WebsocketReceiveEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class WebsocketEventListener implements ApplicationListener<WebsocketReceiveEvent> {
    @Override
    public void onApplicationEvent(WebsocketReceiveEvent event) {

    }
}
