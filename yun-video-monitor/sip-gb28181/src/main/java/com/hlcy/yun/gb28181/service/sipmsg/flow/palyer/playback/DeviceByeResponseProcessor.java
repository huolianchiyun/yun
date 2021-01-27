package com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;

import javax.sip.ResponseEvent;

/**
 * 客户端主动发起的历史视音频回放流程: 20<br/>
 * <p>
 * 20:媒体流发送者收到BYE消息后回复200OK响应,会话断开。
 * </P>
 */
public class DeviceByeResponseProcessor extends FlowResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
    }

    @Override
    public void cleanupContext(ResponseEvent event) {
        FlowContextCacheUtil.remove(getCallId(event));
        getContext(event).clearSessionCache();
    }
}
