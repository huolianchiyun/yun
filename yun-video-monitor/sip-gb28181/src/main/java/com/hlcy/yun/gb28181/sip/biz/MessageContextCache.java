package com.hlcy.yun.gb28181.sip.biz;

import com.hlcy.yun.gb28181.sip.message.handler.MessageContextManager;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.sip.message.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MessageContextCache implements MessageContextManager {

    public MessageContextCache() {
        ResponseHandler.setContextManager(this);
        RequestHandler.setContextManager(this);
    }
}
