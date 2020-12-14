package com.hlcy.yun.sip.message;

import javax.sip.RequestEvent;

/**
 * @implNote 子类要保证线程安全
 */
public abstract class RequestHandler {
    protected String name;

    protected RequestHandler prev;

    protected RequestHandler next;

    protected abstract void handle(RequestEvent event);

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
