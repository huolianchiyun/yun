package com.hlcy.yun.gb28181.sip.biz;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

public interface RegisterProcessor {

    void register(RequestEvent event);

    void logout(Request request);
}
