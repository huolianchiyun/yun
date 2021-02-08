package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;

import javax.sip.ResponseEvent;


public class MediaByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
    }

    @Override
    public void cleanupContext(ResponseEvent event) {
        getContext(event).clearSessionCache();
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
    }
}
