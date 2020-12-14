package com.hlcy.yun.sip.message;

import javax.sip.ResponseEvent;

/**
 * @implNote 子类要保证线程安全
 */
public abstract class ResponseHandler {
    private String name;

    protected ResponseHandler prev;

    protected ResponseHandler next;

    protected abstract void handle(ResponseEvent evt);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
