package com.hlcy.yun.gb28181.operation.response.flow.message.notify;

import com.hlcy.yun.gb28181.operation.response.flow.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

@Slf4j
public class MobilePositionNotifyProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MobilePosition> request, message: {}.", event.getRequest());
        }
    }
}
