package com.hlcy.yun.gb28181.service.sipmsg;

import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Slf4j
public final class MANSCDPXmlParser {

    public static String getCmdTypeFrom(RequestEvent event) {
        return XmlUtil.getTextOfChildTagFrom(getRootElementFrom(event), "CmdType");
    }

    public static Element getRootElementFrom(RequestEvent event) {
        return getRootElementFrom(event.getRequest());
    }

    public static Element getRootElementFrom(Request request) {
        return getRootElementFrom(request.getRawContent());
    }

    public static Element getRootElementFrom(Response response) {
        return getRootElementFrom(response.getRawContent());
    }

    private static Element getRootElementFrom(byte[] content) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("gbk");
        try {
            return XmlUtil.getDocument(content).getRootElement();
        } catch (DocumentException e) {
            log.error("Parse XML-Content Exception, cause: {}.", e.getMessage());
            throw new RuntimeException("Parse XML-Content Exception", e);
        }
    }

    public static String getSN(Request request) {
        final Element rootElement = getRootElementFrom(request);
        return XmlUtil.getTextOfChildTagFrom(rootElement, "SN");
    }
}
