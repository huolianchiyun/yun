package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.AlarmEvent;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Response;
import java.text.ParseException;

import static com.hlcy.yun.gb28181.notification.PublisherFactory.getDeviceEventPublisher;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

@Slf4j
public class AlarmNotifyRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Alarm> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);

        getDeviceEventPublisher().publishEvent(
                new AlarmEvent(getTextOfChildTagFrom(rootElement, "DeviceID"))
                        .setAlarmPriority(getTextOfChildTagFrom(rootElement, "AlarmPriority"))
                        .setAlarmMethod(getTextOfChildTagFrom(rootElement, "AlarmMethod"))
                        .setAlarmTime(getTextOfChildTagFrom(rootElement, "AlarmTime"))
                        .setAlarmDescription(getTextOfChildTagFrom(rootElement, "AlarmDescription"))
                        .setLongitude(getTextOfChildTagFrom(rootElement, "Longitude"))
                        .setLatitude(getTextOfChildTagFrom(rootElement, "Latitude"))
        );
    }

    @Override
    protected void send200Response(RequestEvent event) {
        Element rootElement = getRootElementFrom(event);
        final String sn = getTextOfChildTagFrom(rootElement, "SN");
        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
        try {

            final Response response = buildResponse(Response.OK, event.getRequest());
            String body = new StringBuilder(200)
                    .append("<?xml version=\"1.0\" ?>")
                    .append("<Response>")
                    .append("<CmdType>").append("Alarm").append("</CmdType>")
                    .append("<SN>").append(sn).append("</SN>")
                    .append("<DeviceID>").append(deviceId).append("</DeviceID>")
                    .append("<Result>").append("OK").append("</Result>")
                    .append("</Response>")
                    .toString();

            response.setContent(body, (ContentTypeHeader) response.getHeader(ContentTypeHeader.NAME));
            sendResponse(event, response);
        } catch (ParseException e) {
            log.error("200 Responding to request event failed, cause: {}", e.getMessage());
        }
    }
}
