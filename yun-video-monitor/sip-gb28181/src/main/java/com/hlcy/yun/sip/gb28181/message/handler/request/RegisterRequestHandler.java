package com.hlcy.yun.sip.gb28181.message.handler.request;

import com.hlcy.yun.sip.gb28181.auth.DigestServerAuthHelper;
import com.hlcy.yun.sip.gb28181.bean.Device;
import com.hlcy.yun.sip.gb28181.message.RequestHandler;
import com.hlcy.yun.sip.gb28181.notification.event.LogoutEvent;
import com.hlcy.yun.sip.gb28181.notification.event.RegisterEvent;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Expires;
import lombok.extern.slf4j.Slf4j;
import javax.sip.RequestEvent;
import javax.sip.header.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import static com.hlcy.yun.sip.gb28181.notification.PublisherFactory.getDeviceEventPublisher;

@Slf4j
public class RegisterRequestHandler extends RequestHandler {

    @Override
    public void handle(RequestEvent event) {
        if (!Request.REGISTER.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }

        log.info("Receive a register request: {}.", event.getRequest());
        try {
            if (checkAuthorizedRequest(event)) {
                Request request = event.getRequest();
                Response response = buildResponse(Response.OK, request);
                setupResponseHeaders(request, response);
                sendResponse(event, response);

                Device device = extractDeviceInfoFromRequest(request);
                if (isLogout(request)) {
                    // 注销处理
                    log.info("Logout request, the deviceId: {}.", device.getDeviceId());
                    getDeviceEventPublisher().publishEvent(new LogoutEvent(device.getDeviceId()));
                    return;
                }
                // 注册处理
                log.info("Register request, deviceId: {}.", device.getDeviceId());
                getDeviceEventPublisher().publishEvent(new RegisterEvent(device));
            }
        } catch (ParseException e) {
            log.error("Handle a register request({}) failed, cause: {}.", event, e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isLogout(Request request) {
        ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(Expires.NAME);
        return expiresHeader != null && expiresHeader.getExpires() == 0;
    }

    private Device extractDeviceInfoFromRequest(Request request) {
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

    private void setupResponseHeaders(Request request, Response response) {
        // 添加date头
        response.addHeader(getHeaderFactory().createDateHeader(Calendar.getInstance(Locale.ENGLISH)));
        // 添加Contact头
        response.addHeader(request.getHeader(ContactHeader.NAME));
        // 添加Expires头
        response.addHeader(request.getExpires());
    }

    /**
     * Check if the request has authentication
     *
     * @param event request event
     * @return true if the request has authentication, Otherwise false.
     * @throws ParseException
     */
    private boolean checkAuthorizedRequest(RequestEvent event) throws ParseException {
        boolean isPass = true;
        final Request request = event.getRequest();
        // 未携带授权头或者密码错误 均回复 401
        if (request.getHeader(AuthorizationHeader.NAME) == null) {
            log.warn("Will reply 401 after not carrying authorization.");
            isPass = false;
        } else if (DigestServerAuthHelper.authenticatePlainTextPassword(request)) {
            log.warn("Will reply 401 after sip sever's password isn't correct.");
            isPass = false;
        }
        if (!isPass) {
            final Response response = buildResponse(Response.UNAUTHORIZED, request);
            DigestServerAuthHelper.generateChallenge(getHeaderFactory(), response);
            sendResponse(event, response);
        }
        return isPass;
    }
}
