package com.hlcy.yun.gb28181.operation.response.flow.notify;

import com.hlcy.yun.gb28181.operation.response.flow.query.MessageQueryProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

@Slf4j
public class BroadcastNotifyProcessor extends MessageQueryProcessor {

    @Override
    protected void process(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Broadcast> request, message: {}.", event.getRequest());
        }
    }
}
