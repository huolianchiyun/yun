package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class DefaultMessageRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (event.getRequest().getContentLength().getContentLength() > 0) {
            Element rootElement = getRootElementFrom(event);
            String SN = getTextOfChildTagFrom(rootElement, "SN");
            final FlowContext context = FlowContextCacheUtil.get(SN);
            if (context != null) {
                String result = getTextOfChildTagFrom(rootElement, "Result");
                final String callbackKey = context.getOperationalParams().getCallbackKey();
                if ("Error".equalsIgnoreCase(result)) {
                    DeferredResultHolder.setErrorDeferredResultForRequest(callbackKey, getTextOfChildTagFrom(rootElement, "Reason"));
                } else {
                    DeferredResultHolder.setDeferredResultForRequest(callbackKey, "ok");
                }
            }
        }
    }

    @Override
    protected void cleanupContext(RequestEvent event) {
        FlowContextCacheUtil.remove(MANSCDPXmlParser.getSN(event.getRequest()));
    }
}
