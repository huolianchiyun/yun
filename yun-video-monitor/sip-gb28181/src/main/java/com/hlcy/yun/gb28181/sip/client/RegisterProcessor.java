package com.hlcy.yun.gb28181.sip.client;

import javax.sip.message.Request;

public interface RegisterProcessor {

    void register(Request request);

    void logout(Request request);
}
