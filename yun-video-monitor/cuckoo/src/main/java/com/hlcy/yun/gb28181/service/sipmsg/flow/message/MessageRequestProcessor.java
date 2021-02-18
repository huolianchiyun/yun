package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

import com.hlcy.yun.gb28181.service.sipmsg.MANSCDPXmlParser;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sip.RequestEvent;
import javax.sip.message.Request;
import java.io.ByteArrayInputStream;
import java.text.ParseException;

@Slf4j
public abstract class MessageRequestProcessor extends FlowRequestProcessor {

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
