package com.hlcy.yun.gb28181.service.sipmsg.flow.message;

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
        }
    }

    /**
     * 此方法仅写业务逻辑，无需回复 200 ok，系统默认自动回复
     *
     * @param event /
     */
    protected abstract void doProcess(RequestEvent event);

    public static String getCmdTypeFrom(RequestEvent event) {
        return XmlUtil.getTextOfChildTagFrom(getRootElementFrom(event), "CmdType");
    }

    protected static Element getRootElementFrom(RequestEvent event) {
        Request request = event.getRequest();
        SAXReader reader = new SAXReader();
        reader.setEncoding("gbk");
        Document xml;
        try {
            xml = reader.read(new ByteArrayInputStream(request.getRawContent()));
        } catch (DocumentException e) {
            log.error("Parse a message request({}) XML-Content Exception, cause: {}.", event, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(String.format("Parse a message request(%s) XML-Content Exception", event.getRequest()), e);
        }
        return xml.getRootElement();
    }
}
