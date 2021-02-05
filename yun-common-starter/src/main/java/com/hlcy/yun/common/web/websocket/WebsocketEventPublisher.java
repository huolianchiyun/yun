package com.hlcy.yun.common.web.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(WebsocketReceiveEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
