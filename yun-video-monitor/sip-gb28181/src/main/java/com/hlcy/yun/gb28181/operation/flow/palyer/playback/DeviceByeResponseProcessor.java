package com.hlcy.yun.gb28181.operation.flow.palyer.playback;

import com.hlcy.yun.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.gb28181.operation.flow.FlowContext;
import com.hlcy.yun.gb28181.operation.flow.FlowContextCache;

import javax.sip.ResponseEvent;

/**
 * 客户端主动发起的历史视音频回放流程: 20<br/>
 * <P>
 * 20:媒体流发送者收到BYE消息后回复200OK响应,会话断开。
 * </P>
 */
public class DeviceByeResponseProcessor extends ResponseProcessor {
    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        // clean up play flow environment
        FlowContextCache.remove(getCallId(event));
        getFlowContext(event).clearSessionCache();
    }
}