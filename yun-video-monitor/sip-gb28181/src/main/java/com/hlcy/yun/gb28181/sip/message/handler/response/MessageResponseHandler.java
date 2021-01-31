package com.hlcy.yun.gb28181.sip.message.handler.response;

import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

import javax.sip.ResponseEvent;

@Slf4j
public class MessageResponseHandler extends ResponseHandler {
    public MessageResponseHandler(String name, String method) {
        super(name, method);
    }

    @Override
    public void doHandle(ResponseEvent event) {
        log.info("Receive a message response: \n{}", event.getResponse());
    }
}
