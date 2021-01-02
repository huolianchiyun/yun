package com.hlcy.yun.gb28181.sip.message.handler.request;

import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Slf4j
public class SubscribeRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        Request request = event.getRequest();
        if (!Request.SUBSCRIBE.equals(request.getMethod())) {
            this.next.handle(event);
            return;
        }

        log.info("Receive a subscribe request: {}", event.getRequest());
        try {
            Response response = buildResponse(Response.OK, request);
            if (response != null) {
                ExpiresHeader expireHeader = null;
                    expireHeader = getHeaderFactory().createExpiresHeader(30);
                response.setExpires(expireHeader);
            }
            if (log.isDebugEnabled()) {
                log.debug("Response: ({}) of the subscribe request: {}", response, request);
            }
            sendResponse(event, response);
        }catch (InvalidArgumentException | ParseException e) {
            log.error("Handle a subscribe request({}) failed, cause: {}", event, e.getMessage());
            e.printStackTrace();
        }
    }
}
