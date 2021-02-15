package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class MediaByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
    }

    @Override
    public void cleanupContext(ResponseEvent event) {
        final FlowContext context = getContext(event);
        context.clearSessionCache();
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        log.info("SSRC：{}， close voice broadcast successfully.", context.getSsrc());
    }
}
