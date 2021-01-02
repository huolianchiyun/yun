package com.hlcy.yun.gb28181.sip.message;

import javax.sip.RequestEvent;

public interface RequestInvoker {
    void processRequest(RequestEvent event);
}
