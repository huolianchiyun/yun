package com.hlcy.yun.gb28181.service.sipmsg.flow.message.subscribe;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import javax.sip.ResponseEvent;

import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class SubscribeResponseProcessor extends ResponseProcessor<FlowContext> {

    @Override
    protected void process(ResponseEvent event, FlowContext context) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a subscribe response, message: \n{}", event.getResponse());
        }
        Element rootElement = getRootElementFrom(event);
        final String callbackKey = context.getOperationalParams().getCallbackKey();
        if ("Error".equalsIgnoreCase(getTextOfChildTagFrom(rootElement, "Result"))) {
            DeferredResultHolder.setErrorDeferredResultForRequest(callbackKey, getTextOfChildTagFrom(rootElement, "Reason"));
        } else {
            DeferredResultHolder.setDeferredResultForRequest(callbackKey, "OK");
        }
    }

    private Element getRootElementFrom(ResponseEvent event) {
        return MANSCDPXmlParser.getRootElementFrom(event.getResponse());
    }

    @Override
    protected void cleanupContext(ResponseEvent event) {
        FlowContextCacheUtil.remove(getCallId(event.getResponse()));
    }

    @Override
    protected FlowContext getContext(ResponseEvent event) {
        return FlowContextCacheUtil.get(getCallId(event.getResponse()));
    }
}
