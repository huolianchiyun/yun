package com.hlcy.yun.gb28181.sip.message.factory;

import com.hlcy.yun.gb28181.util.UUIDUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
class From extends Address {

    /**
     * From constructor
     *
     * @param user /
     * @param host /
     * @param tag  this value may be null, but not empty.
     */
    From(String user, Host host, String tag) {
        this(user, host);
        this.tag = tag;
    }

    From(String user, Host host) {
        super(user, host);
        this.tag = UUIDUtil.getUUID();
    }
}
