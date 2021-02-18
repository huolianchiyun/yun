package com.hlcy.yun.gb28181.service.command.control;


import com.hlcy.yun.gb28181.service.params.control.HomePositionControlParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 看守位控制命令
 */
public class HomePositionCmd extends AbstractControlCmd<HomePositionControlParams> {

    public HomePositionCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(HomePositionControlParams homePositionControlParams) {
        return new StringBuilder(200)
                .append("<HomePosition>")
                // 看守位使能1:开启,0:关闭(必选)
                .append("<Enabled>").append(homePositionControlParams.getEnabled()).append("</Enabled>")
                // 自动归位时间间隔,开启看守位时使用,单位:秒(s)(可选)
                .append("<ResetTime>").append(homePositionControlParams.getResetTime()).append("</ResetTime>")
                // 调用预置位编号,开启看守位时使用,取值范围0~255(可选)
                .append("<PresetIndex>").append(homePositionControlParams.getPresetIndex()).append("</PresetIndex>")
                .append("</HomePosition>")
                .toString();
    }

    @Override
    protected void cacheFlowContext(HomePositionControlParams homePositionControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(
                Operation.HOME_POSITION,
                homePositionControlParams,
                new DefaultMessageRequestProcessor(),
                new DefaultMessageResponseProcessor().setResponseCallback(false));
        FlowContextCacheUtil.put(getCallId(request), flowContext);
        FlowContextCacheUtil.put(MANSCDPXmlParser.getSN(request), flowContext);
    }
}
