package com.hlcy.yun.gb28181.service.listener;

import com.hlcy.yun.gb28181.sip.subscribe.SipEventListener;

import javax.sip.TimeoutEvent;

public class TimeoutEventListener extends SipEventListener<TimeoutEvent> {
    TimeoutEventListener() {
        super(TimeoutEvent.class);
    }

    @Override
    public void handleEvent(TimeoutEvent event) {

    }
}
