package com.hlcy.yun.gb28181.service.sipmsg;

import com.hlcy.yun.gb28181.bean.DeviceInfo;
import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.LogoutEvent;
import com.hlcy.yun.gb28181.notification.event.RegisterEvent;
import com.hlcy.yun.gb28181.sip.biz.RegisterProcessor;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;
import lombok.extern.slf4j.Slf4j;

import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

@Slf4j
public class RegisterProcessorImpl implements RegisterProcessor {

    @Override
    public void register(Request request) {
        final DeviceInfo device = extractDeviceInfoFrom(request);
        log.info("Register request, deviceId: {}.", device.getDeviceId());
        PublisherFactory.getDeviceEventPublisher()
                .publishEvent(new RegisterEvent(device));
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
        SIPRequest sipRequest = (SIPRequest) request;
        return new DeviceInfo()
                .setDeviceId(deviceId)
                .setIp(viaHeader.getHost())
                .setProxyIp(sipRequest.getRemoteAddress().getHostAddress())
                .setPort(sipRequest.getPeerPacketSourcePort())
                .setTransport(viaHeader.getTransport());
    }
}
