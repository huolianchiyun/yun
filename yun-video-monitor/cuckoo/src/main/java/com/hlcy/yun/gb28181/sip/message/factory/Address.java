package com.hlcy.yun.gb28181.sip.message.factory;

import lombok.Getter;

@Getter
public class Address {
    protected String user;
    protected Host host;
    protected String tag;

    /**
     * Address constructor
     *
     * @param user /
     * @param host /
     * @param tag  this value may be null, but not empty.
     */
    Address(String user, Host host, String tag) {
        this(user, host);
        this.tag = tag;
    }

    Address(String user, Host host) {
        this.user = user;
        this.host = host;
    }
}
