package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import javax.sip.RequestEvent;

@Slf4j
public abstract class MessageRequestProcessor extends FlowRequestProcessor {
    protected static final String CLIENT_RESPONSE_REQUEST_RESULT_SUCCESS = "OK";
    protected static final String CLIENT_RESPONSE_REQUEST_RESULT_ERROR = "ERROR";

    @Override
    protected void process(RequestEvent event, FlowContext context) {
        try {
            doProcess(event);
        } finally {
            // 200 with no response body
            send200Response(event);
            cleanupContext(event);
        }
    }

    /**
     * 此方法仅写业务逻辑，无需回复 200 ok，系统默认自动回复
     *
     * @param event /
     */
    protected abstract void doProcess(RequestEvent event);

    protected void cleanupContext(RequestEvent event){

    }

    protected String getCmdTypeFrom(RequestEvent event) {
        return MANSCDPXmlParser.getCmdTypeFrom(event);
    }

    protected Element getRootElementFrom(RequestEvent event) {
        return MANSCDPXmlParser.getRootElementFrom(event);
    }
}
