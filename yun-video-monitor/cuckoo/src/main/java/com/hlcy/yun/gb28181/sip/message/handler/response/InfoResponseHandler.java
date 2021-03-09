package com.hlcy.yun.gb28181.sip.message.handler.response;

import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class InfoResponseHandler extends ResponseHandler {
    public InfoResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    public void doHandle(ResponseEvent event) {
        log.info("Receive a info response: \n{}", event.getResponse());
    }
}
