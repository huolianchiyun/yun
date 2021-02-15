package com.hlcy.yun.gb28181.service.listener;

import com.hlcy.yun.gb28181.sip.subscribe.SipEventListener;

import javax.sip.DialogTerminatedEvent;

public class DialogTerminatedEventListener extends SipEventListener<DialogTerminatedEvent> {
    DialogTerminatedEventListener() {
        super(DialogTerminatedEvent.class);
    }

    @Override
    public void handleEvent(DialogTerminatedEvent event) {
        final String callId = event.getDialog().getCallId().getCallId();
    }
}
