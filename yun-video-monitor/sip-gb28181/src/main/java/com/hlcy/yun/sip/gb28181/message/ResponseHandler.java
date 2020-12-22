package com.hlcy.yun.sip.gb28181.message;

import javax.sip.ResponseEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Response;

/**
 * @implNote 子类要保证线程安全
 */
public abstract class ResponseHandler {
    private String name;

    protected ResponseHandler prev;

    protected ResponseHandler next;

    public abstract void handle(ResponseEvent event);

    protected String getMethodFrom(ResponseEvent event) {
        return ((CSeqHeader) event.getResponse().getHeader(CSeqHeader.NAME)).getMethod();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
