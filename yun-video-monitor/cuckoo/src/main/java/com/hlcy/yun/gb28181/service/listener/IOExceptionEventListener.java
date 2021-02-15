package com.hlcy.yun.gb28181.service.listener;

import com.hlcy.yun.gb28181.sip.subscribe.SipEventListener;
import javax.sip.IOExceptionEvent;

public class IOExceptionEventListener extends SipEventListener<IOExceptionEvent> {

    IOExceptionEventListener() {
        super(IOExceptionEvent.class);
    }

    @Override
    public void handleEvent(IOExceptionEvent event) {

    }

    public static void main(String[] args) {
        final IOExceptionEventListener ioExceptionEventListener = new IOExceptionEventListener();
        System.out.println(ioExceptionEventListener.getEventType());
    }
}
