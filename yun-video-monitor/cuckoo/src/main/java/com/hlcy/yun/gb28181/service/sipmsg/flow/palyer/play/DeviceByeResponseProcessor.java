package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.ssrc.SSRCManger;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class DeviceByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        SSRCManger.releaseSSRC(context.getSsrc());
        // clean up play flow environment
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        FlowContextCacheUtil.remove(context.getSsrc());
        context.clearSessionCache();
        log.info("*** Play SSRC: {} is closed successfully ***", context.getSsrc());
    }
}
