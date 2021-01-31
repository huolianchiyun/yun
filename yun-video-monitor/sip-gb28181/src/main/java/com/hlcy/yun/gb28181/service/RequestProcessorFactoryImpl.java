package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowPipelineFactory;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessorFactory;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

public class RequestProcessorFactoryImpl implements RequestProcessorFactory {

    @Override
    public RequestProcessor getRequestProcessor(RequestEvent event) {
        if (Request.MESSAGE.equals(event.getRequest().getMethod())) {
            final String cmdTypeFrom = MessageProcessor.getCmdTypeFrom(event);

            final Operation operation = Operation.valueOf(cmdTypeFrom.toUpperCase());
            return FlowPipelineFactory.getRequestFlowPipeline(operation).first();
        }
        return null;
    }
}
