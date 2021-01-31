package com.hlcy.yun.gb28181.service.sipmsg;

import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.LogoutEvent;
import com.hlcy.yun.gb28181.notification.event.RegisterEvent;
import com.hlcy.yun.gb28181.sip.biz.RegisterProcessor;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import lombok.extern.slf4j.Slf4j;

import javax.sip.header.FromHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

@Slf4j
public class RegisterProcessorImpl implements RegisterProcessor {

    @Override
    public void register(Request request) {
        final Device device = extractDeviceInfoFrom(request);
        log.info("Register request, deviceId: {}.", device.getDeviceId());
        PublisherFactory.getDeviceEventPublisher().publishEvent(new RegisterEvent(device));
    }

    @Override
    public void logout(Request request) {
        final Device device = extractDeviceInfoFrom(request);
        log.info("Logout request, the deviceId: {}.", device.getDeviceId());
        PublisherFactory.getDeviceEventPublisher().publishEvent(new LogoutEvent(device.getDeviceId()));
    }

    private Device extractDeviceInfoFrom(Request request) {
        ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
        String received = viaHeader.getReceived();
        int rPort = viaHeader.getRPort();
        FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
        AddressImpl address = (AddressImpl) fromHeader.getAddress();
        SipUri uri = (SipUri) address.getURI();
        String deviceId = uri.getUser();
        return new Device(deviceId)
                .setIp(viaHeader.getReceived())
                .setPort(viaHeader.getRPort())
                .setAddress(received.concat(":").concat(String.valueOf(rPort)))
                .setTransport(((ViaHeader) request.getHeader(ViaHeader.NAME)).getTransport());
    }
}
