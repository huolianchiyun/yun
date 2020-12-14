package com.hlcy.yun.sip.message;

import javax.sip.ResponseEvent;

public interface ResponseInvoker {
    void processResponse(ResponseEvent event);
}
