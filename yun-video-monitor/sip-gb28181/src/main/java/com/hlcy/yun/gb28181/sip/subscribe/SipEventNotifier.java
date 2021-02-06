package com.hlcy.yun.gb28181.sip.subscribe;

import cn.hutool.core.collection.ConcurrentHashSet;

import java.util.EventObject;
import java.util.Set;

public final class SipEventNotifier{
    private static final Set<SipEventListener> listeners= new ConcurrentHashSet<>(5);

    public static void addListener(SipEventListener listener){
        listeners.add(listener);
    }

    public static void removeListener(SipEventListener listener){
        listeners.remove(listener);
    }

    public static void notify(EventObject event){
        listeners.forEach(listener->{
            if (event.getClass().isAssignableFrom(listener.getEventType())) {
                listener.handleEvent(event);
            }
        });
    }
}
