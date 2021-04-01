package com.hlcy.yun.gb28181.sip.biz;

import javax.sip.message.Request;

public interface RegisterProcessor {

    void register(Request event);

    void logout(Request request);
}
