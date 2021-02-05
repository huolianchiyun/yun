package com.hlcy.yun.common.web.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Accessors(chain = true)
public class WebsocketReceiveEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public WebsocketReceiveEvent(Object source) {
        super(source);
    }

    private String uid;

    private String message;
}
