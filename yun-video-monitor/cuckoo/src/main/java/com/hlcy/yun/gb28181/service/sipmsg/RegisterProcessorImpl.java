package com.hlcy.yun.gb28181.service.sipmsg;

import com.hlcy.yun.gb28181.bean.DeviceInfo;
import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.LogoutEvent;
import com.hlcy.yun.gb28181.notification.event.RegisterEvent;
import com.hlcy.yun.gb28181.sip.biz.RegisterProcessor;
import gov.nist.javax.sip.RequestEventExt;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

@Slf4j
public class RegisterProcessorImpl implements RegisterProcessor {

    @Override
    public void register(RequestEvent event) {
        final DeviceInfo device = extractDeviceInfoFrom(event.getRequest());
        log.info("Register request, deviceId: {}.", device.getDeviceId());
        RequestEventExt eventExt = (RequestEventExt) event;
        PublisherFactory.getDeviceEventPublisher()
                .publishEvent(new RegisterEvent(device
                        .setProxyIp(eventExt.getRemoteIpAddress())
                        .setPort(eventExt.getRemotePort())));
    }

    @Override
    public void logout(Request request) {
        final DeviceInfo device = extractDeviceInfoFrom(request);
        log.info("Logout request, the deviceId: {}.", device.getDeviceId());
        PublisherFactory.getDeviceEventPublisher().publishEvent(new LogoutEvent(device.getDeviceId()));
    }

    private DeviceInfo extractDeviceInfoFrom(Request request) {
        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
        AddressImpl address = (AddressImpl) fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        String deviceId = uri.getUser();
        ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        return new DeviceInfo().setDeviceId(deviceId)
                .setIp(viaHeader.getHost())
                .setTransport(viaHeader.getTransport());
    }
}
