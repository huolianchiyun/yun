package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.KeepaliveEvent;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class KeepaliveNotifyRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <KeepAlive> request: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);

        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
        final SIPRequest sipRequest = (SIPRequest) event.getRequest();
        PublisherFactory.getDeviceEventPublisher()
                .publishEvent(new KeepaliveEvent(deviceId)
                        .setProxyIp(sipRequest.getPeerPacketSourceAddress().getHostAddress())
                        .setDevicePort(sipRequest.getPeerPacketSourcePort()));
    }
}
