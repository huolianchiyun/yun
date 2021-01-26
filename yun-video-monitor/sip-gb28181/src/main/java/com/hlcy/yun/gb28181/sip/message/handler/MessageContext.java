package com.hlcy.yun.gb28181.sip.message.handler;

import com.hlcy.yun.gb28181.sip.client.RequestProcessor;
import com.hlcy.yun.gb28181.sip.client.ResponseProcessor;

public abstract class MessageContext {

    protected RequestProcessor currentRequestProcessor;

    protected ResponseProcessor currentResponseProcessor;

    public abstract RequestProcessor getRequestProcessor();

    public abstract ResponseProcessor getResponseProcessor();

    public abstract void setCurrentRequestProcessor2next();

    public abstract void setCurrentResponseProcessor2next();
}
