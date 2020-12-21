package com.hlcy.yun.sip.gb28181.message.handler.request;

import com.hlcy.yun.sip.gb28181.message.RequestHandler;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

public class ByeRequestHandler extends RequestHandler {
    @Override
    public void handle(RequestEvent event) {
        if (!Request.BYE.equals(event.getRequest().getMethod())){
            this.next.handle(event);
            return;
        }
        // Todo 处理业务逻辑
    }
}
