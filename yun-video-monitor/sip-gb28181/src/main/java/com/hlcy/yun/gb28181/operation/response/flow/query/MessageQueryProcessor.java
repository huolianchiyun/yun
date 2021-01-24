package com.hlcy.yun.gb28181.operation.response.flow.query;

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

@Slf4j
public abstract class MessageQueryProcessor extends RequestProcessor {

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
