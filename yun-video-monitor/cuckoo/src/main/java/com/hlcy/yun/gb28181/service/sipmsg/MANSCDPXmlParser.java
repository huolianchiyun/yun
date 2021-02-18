package com.hlcy.yun.gb28181.service.sipmsg;

import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Slf4j
public final class MANSCDPXmlParser {

    public static String getCmdTypeFrom(RequestEvent event) {
        return XmlUtil.getTextOfChildTagFrom(getRootElementFrom(event), "CmdType");
    }

    public static Element getRootElementFrom(RequestEvent event) {
        return getRootElementFrom(event.getRequest());
    }

    public static Element getRootElementFrom(Request request) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("gbk");
        try {
            return XmlUtil.getDocument(request.getRawContent()).getRootElement();
        } catch (DocumentException e) {
            log.error("Parse a message request({}) XML-Content Exception, cause: {}.", request, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(String.format("Parse a message request(%s) XML-Content Exception", request), e);
        }
    }

    public static String getSN(Request request) {
        final Element rootElement = getRootElementFrom(request);
        return XmlUtil.getTextOfChildTagFrom(rootElement, "SN");
    }
}
