package com.hlcy.yun.gb28181.service.listener;

import com.hlcy.yun.gb28181.sip.subscribe.SipEventNotifier;

public final class SipEventListenerManager {

    public static void register() {
        SipEventNotifier.addListener(new DialogTerminatedEventListener());
        SipEventNotifier.addListener(new IOExceptionEventListener());
        SipEventNotifier.addListener(new TimeoutEventListener());
    }
}
