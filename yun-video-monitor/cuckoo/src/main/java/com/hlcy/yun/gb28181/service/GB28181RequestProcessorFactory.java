package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowPipelineFactory;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessorFactory;
import com.hlcy.yun.gb28181.sip.message.DefaultPipeline;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;

import javax.sdp.SdpException;
import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;
import javax.sip.RequestEvent;
import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.BROADCAST;

public class GB28181RequestProcessorFactory implements RequestProcessorFactory {

    @Override
    public RequestProcessor getRequestProcessor(RequestEvent event) {
        final String method = event.getRequest().getMethod();
        if (Request.MESSAGE.equals(method)) {
            final String cmdType = MANSCDPXmlParser.getCmdTypeFrom(event);
            final Operation operation = Operation.get(cmdType);
            if (operation != null) {
                final DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> pipeline = FlowPipelineFactory.getRequestFlowPipeline(operation);
                if (pipeline != null) {
                    return pipeline.get(operation.code());
                }
            }

            final FlowContext flowContext = FlowContextCacheUtil.get(MANSCDPXmlParser.getSN(event.getRequest()));
            if (flowContext != null) {
                return flowContext.requestProcessor();
            }
        } else if (Request.INVITE.equals(method)) {
            //TODO 后续优化根据策略获取响应的 invite request processor
            try {
                // get voice broadcast flow context
                final SessionDescription sdp = getSessionDescription(new String(event.getRequest().getRawContent()));
                if (sdp.getMediaDescriptions(false).toString().contains("audio")) {
                    FlowContextCacheUtil.setNewKey(
                            BROADCAST.name() + sdp.getOrigin().getUsername(),
                            SipRequestFactory.getCallId(event.getRequest()));
                    return FlowPipelineFactory.getRequestFlowPipeline(Operation.BROADCAST).get(Request.INVITE);
                }
            } catch (SdpException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private SessionDescription getSessionDescription(String body) throws SdpParseException {
        return SdpFactory.getInstance().createSessionDescription(body);
    }
}
