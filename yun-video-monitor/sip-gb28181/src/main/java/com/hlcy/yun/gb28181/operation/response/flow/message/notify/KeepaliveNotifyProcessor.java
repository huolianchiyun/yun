package com.hlcy.yun.gb28181.operation.response.flow.message.notify;

import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.KeepaliveEvent;
import com.hlcy.yun.gb28181.operation.response.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.text.ParseException;

@Slf4j
public class KeepaliveNotifyProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <KeepAlive> request: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        PublisherFactory.getDeviceEventPublisher().publishEvent(new KeepaliveEvent(deviceId));
    }
}
