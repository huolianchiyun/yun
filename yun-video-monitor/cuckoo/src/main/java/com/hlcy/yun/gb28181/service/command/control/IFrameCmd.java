package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.IFrameControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 强制关键帧命令,设备收到此命令应立刻发送一个IDR帧(可选)
 */
public class IFrameCmd extends AbstractControlCmd<IFrameControlParams> {

    public IFrameCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(IFrameControlParams iFrameControlParams) {
        return "<IFameCmd>Send</IFameCmd>";
    }

    @Override
    protected void cacheFlowContext(IFrameControlParams iFrameControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.IFRAME, iFrameControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
