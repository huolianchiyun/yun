package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.auth.DigestServerAuthHelper;
import com.hlcy.yun.gb28181.sip.biz.RegisterProcessor;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import gov.nist.javax.sip.header.Expires;
import lombok.extern.slf4j.Slf4j;
import javax.sip.RequestEvent;
import javax.sip.header.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

@Slf4j
public class RegisterRequestHandler extends RequestHandler {
    private static RegisterProcessor registerProcessor;

    @Override
    public void handle(RequestEvent event) {
        if (!Request.REGISTER.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Receive a register request: {}.", event.getRequest());
        }
        try {
            if (checkAuthorizedRequest(event)) {
                sendResponseFor(event);

                Request request = event.getRequest();
                if (isLogout(request)) {
                    // 注销处理
                    registerProcessor.logout(request);
                    return;
                }
                // 注册处理
                registerProcessor.register(request);
            }
        } catch (ParseException e) {
            log.error("Handle a register request({}) failed, cause: {}.", event, e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendResponseFor(RequestEvent event) throws ParseException {
        final Request request = event.getRequest();
        Response response = buildResponse(Response.OK, request);
        // 添加date头
        response.addHeader(getHeaderFactory().createDateHeader(Calendar.getInstance(Locale.ENGLISH)));
        // 添加Contact头
        response.addHeader(request.getHeader(ContactHeader.NAME));
        // 添加Expires头
        response.addHeader(request.getExpires());
        sendResponse(event, response);
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
        if (request.getHeader(ProxyAuthorizationHeader.NAME) == null) {
            log.warn("Will reply 401 after not carrying authorization.");
            isPass = false;
        } else if (!DigestServerAuthHelper.authenticatePlainTextPassword(request)) {
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

    private boolean isLogout(Request request) {
        ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(Expires.NAME);
        return expiresHeader != null && expiresHeader.getExpires() == 0;
    }

    public static void setRegisterProcessor(RegisterProcessor registerProcessor) {
        RegisterRequestHandler.registerProcessor = registerProcessor;
    }
}
