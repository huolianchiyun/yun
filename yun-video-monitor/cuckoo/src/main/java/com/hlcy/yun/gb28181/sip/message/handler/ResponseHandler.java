package com.hlcy.yun.gb28181.sip.message.handler;

import com.hlcy.yun.gb28181.sip.message.MessageHandler;

import javax.sip.ResponseEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;

/**
 * @implNote 子类要保证线程安全
 */
public abstract class ResponseHandler extends MessageHandler<ResponseEvent> {
    private static MessageContextManager contextManager;
    protected String name;

    // 响应方法: invite, message and so on
    protected String method;


    public ResponseHandler(String name, String method) {
        this.name = name;
        this.method = method;
    }

    protected abstract void doHandle(ResponseEvent event);

    public void handle(ResponseEvent event) {
        if (!method.equals(getMethodFrom(event))) {
            this.next.handle(event);
            return;
        }
        doHandle(event);
    }

    protected MessageContext getMessageContext(ResponseEvent event) {
        return contextManager.get(event.getResponse());
    }

    protected String getCallId(ResponseEvent event) {
        return ((CallIdHeader) event.getResponse().getHeader(CallIdHeader.NAME)).getCallId();
    }

    private String getMethodFrom(ResponseEvent event) {
        return ((CSeqHeader) event.getResponse().getHeader(CSeqHeader.NAME)).getMethod();
    }

    public String getName() {
        return name;
    }

    public static void setContextManager(MessageContextManager contextManager) {
        ResponseHandler.contextManager = contextManager;
    }
}
