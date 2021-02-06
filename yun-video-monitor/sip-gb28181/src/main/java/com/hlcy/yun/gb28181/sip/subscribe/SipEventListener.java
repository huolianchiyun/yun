package com.hlcy.yun.gb28181.sip.subscribe;

import lombok.Getter;
import java.util.EventObject;

@Getter
public abstract class SipEventListener<E extends EventObject> {
    private Class<E> eventType;

    public SipEventListener(Class<E> eventType) {
        this.eventType = eventType;
    }

    public abstract void  handleEvent(E event);
}
