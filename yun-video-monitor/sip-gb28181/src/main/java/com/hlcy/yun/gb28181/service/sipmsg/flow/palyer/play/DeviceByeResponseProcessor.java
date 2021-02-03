package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

/**
 * 客户端主动发起的实时视音频点播流程: 20<br/>
 * <p>
 * 20:媒体流发送者收到BYE消息后回复200OK响应,会话断开。
 * </P>
 */
@Slf4j
public class DeviceByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        // clean up play flow environment
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
        getContext(event).clearSessionCache();
        log.info("*** SSRC: {} is closed successfully ***", context.getSsrc());
    }
}
