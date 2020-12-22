package com.hlcy.yun.sip.gb28181.message.handler.response;

import com.hlcy.yun.sip.gb28181.message.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;
import javax.sip.message.Request;

@Slf4j
public class CancelResponseHandler extends ResponseHandler {
    @Override
    public void handle(ResponseEvent event) {
        if (!Request.CANCEL.equals(getMethodFrom(event))) {
            this.next.handle(event);
            return;
        }
        log.info("Receive a cancel response: {}.", event.getResponse());
    }
}
