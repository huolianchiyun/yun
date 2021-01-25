package com.hlcy.yun.gb28181.operation.response.flow.message;

import com.hlcy.yun.gb28181.operation.response.flow.RequestProcessor;
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
public abstract class MessageProcessor extends RequestProcessor {

    @Override
    protected void process(RequestEvent event) {
        doProcess(event);
        try {
            // 200 with no response body
            send200Response(event);
        } catch (ParseException e) {
            log.error("Process a CmdType <{}> message({}) failed, cause: \n{}", getCmdTypeFrom(event), event.getRequest(), e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract void doProcess(RequestEvent event);

    protected String getCmdTypeFrom(RequestEvent event) {
        return XmlUtil.getTextOfChildTagFrom(getRootElementFrom(event), "CmdType");
    }

    protected Element getRootElementFrom(RequestEvent event) {
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
