package com.hlcy.yun.sip.message;

import javax.sip.RequestEvent;

public interface RequestInvoker {
    void processRequest(RequestEvent event);
}
