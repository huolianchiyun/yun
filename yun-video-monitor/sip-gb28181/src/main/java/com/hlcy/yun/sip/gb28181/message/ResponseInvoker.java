package com.hlcy.yun.sip.gb28181.message;

import javax.sip.ResponseEvent;

public interface ResponseInvoker {
    void processResponse(ResponseEvent event);
}
