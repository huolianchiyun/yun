package com.hlcy.yun.gb28181.util;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * 基于dom4j的工具包
 */
@Slf4j
public class XmlUtil {
    /**
     * 解析 XML为 Document 对象
     *
     * @param xml 被解析的 string XMl
     * @return Document
     */
    public static Document parseXml(String xml) {
        Document document = null;
        StringReader sr = new StringReader(xml);
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(sr);
        } catch (DocumentException e) {
            log.error("Parse string xml exception.", e);
        }
        return document;
    }

    /**
     * 获取 child tag element对象的 text的值
     *
     * @param parentTag 节点的对象
     * @param childTag  节点的tag
     * @return text value
     */
    public static String getTextOfChildTagFrom(Element parentTag, String childTag) {
        if (null == parentTag) {
            return null;
        }
        Element e = parentTag.element(childTag);
        return null == e ? "" : e.getText();
    }

    /**
     * 获取 element对象的 text的值
     *
     * @param element 节点的对象
     * @return text value
     */
    public static String getTextOf(Element element) {
        return getOrDefaultTextOf(element, "");
    }

    /**
     * 获取 element对象的 text的值
     *
     * @param element    节点的对象
     * @param defaultVal 节点的对象 text 默认
     * @return text value
     */
    public static String getOrDefaultTextOf(Element element, String defaultVal) {
        return element != null ? element.getText() : (defaultVal == null ? "" : defaultVal);
    }

}
