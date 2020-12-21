package com.hlcy.yun.sip.gb28181.message;

import javax.sip.RequestEvent;

public interface RequestInvoker {
    void processRequest(RequestEvent event);
}
