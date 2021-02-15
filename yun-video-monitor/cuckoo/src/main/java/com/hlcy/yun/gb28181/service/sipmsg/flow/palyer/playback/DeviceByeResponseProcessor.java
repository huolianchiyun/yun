package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;

import javax.sip.ResponseEvent;

public class DeviceByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
    }

    @Override
    public void cleanupContext(ResponseEvent event) {
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        getContext(event).clearSessionCache();
    }
}
