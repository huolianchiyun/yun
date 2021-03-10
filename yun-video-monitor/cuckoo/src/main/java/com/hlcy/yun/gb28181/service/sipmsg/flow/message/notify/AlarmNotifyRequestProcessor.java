package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.common.spring.SpringContextHolder;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.notification.event.AlarmEvent;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestSender;
import com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory;
import com.hlcy.yun.gb28181.sip.message.factory.Transport;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import static com.hlcy.yun.gb28181.notification.PublisherFactory.getDeviceEventPublisher;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createFrom;
import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.createTo;
import static com.hlcy.yun.gb28181.util.XmlUtil.getChild;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class AlarmNotifyRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Alarm> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        final AlarmEvent alarmEvent = new AlarmEvent(getTextOfChildTagFrom(rootElement, "DeviceID"))
                .setAlarmPriority(getTextOfChildTagFrom(rootElement, "AlarmPriority"))
                .setAlarmMethod(getTextOfChildTagFrom(rootElement, "AlarmMethod"))
                .setAlarmTime(getTextOfChildTagFrom(rootElement, "AlarmTime"))
                .setAlarmDescription(getTextOfChildTagFrom(rootElement, "AlarmDescription"))
                .setLongitude(getTextOfChildTagFrom(rootElement, "Longitude"))
                .setLatitude(getTextOfChildTagFrom(rootElement, "Latitude"));

        final Element info = getChild(rootElement, "Info");
        if (info != null) {
            alarmEvent.setAlarmType(getTextOfChildTagFrom(info, "AlarmType"));
            final Element alarmTypeParam = getChild(info, "AlarmTypeParam");
            if (alarmTypeParam != null) {
                alarmEvent.setEventType(getTextOfChildTagFrom(alarmTypeParam, "EventType"));
            }
        }

        getDeviceEventPublisher().publishEvent(alarmEvent);
    }

    @Override
    protected void send200Response(RequestEvent event) {
        super.send200Response(event);

        Element rootElement = getRootElementFrom(event);
        final String sn = getTextOfChildTagFrom(rootElement, "SN");
        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");

        String body = new StringBuilder(200)
                .append("<?xml version=\"1.0\" ?>")
                .append("<Response>")
                .append("<CmdType>").append("Alarm").append("</CmdType>")
                .append("<SN>").append(sn).append("</SN>")
                .append("<DeviceID>").append(deviceId).append("</DeviceID>")
                .append("<Result>").append("OK").append("</Result>")
                .append("</Response>")
                .toString();

        final GB28181Properties properties = SpringContextHolder.getBean(GB28181Properties.class);
        ViaHeader via = (ViaHeader) event.getRequest().getHeader(ViaHeader.NAME);
        final Request request = SipRequestFactory.getMessageRequest(
                createTo(deviceId, via.getHost(), via.getPort()),
                createFrom(properties.getSipId(), properties.getSipIp(), properties.getSipPort()),
                Transport.valueOf(via.getTransport()),
                body.getBytes(StandardCharsets.UTF_8));

        RequestSender.sendRequest(request);
    }
}
