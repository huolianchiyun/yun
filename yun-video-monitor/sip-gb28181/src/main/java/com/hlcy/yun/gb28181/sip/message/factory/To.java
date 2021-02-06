package com.hlcy.yun.gb28181.sip.message.factory;


class To extends Address{

    To(String user, Host host, String tag) {
        super(user, host, tag);
    }

    To(String user, Host host) {
        super(user, host);
    }
}
