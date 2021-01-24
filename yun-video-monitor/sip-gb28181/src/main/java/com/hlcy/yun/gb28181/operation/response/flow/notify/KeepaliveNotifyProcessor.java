package com.hlcy.yun.gb28181.operation.response.flow.notify;

import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.KeepaliveEvent;
import com.hlcy.yun.gb28181.operation.response.flow.query.MessageQueryProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.text.ParseException;

@Slf4j
public class KeepaliveNotifyProcessor extends MessageQueryProcessor {

    @Override
    protected void process(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <KeepAlive> request: {}.", event.getRequest());
        }
        try {
            Element rootElement = getRootElementFrom(event);
            String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
            send200Response(event);
            PublisherFactory.getDeviceEventPublisher().publishEvent(new KeepaliveEvent(deviceId));
        } catch (ParseException e) {
            log.error("Handle a CmdType <KeepAlive> message({}) failed, cause: {}.", event.getRequest(), e.getMessage());
            e.printStackTrace();
        }
    }
}
