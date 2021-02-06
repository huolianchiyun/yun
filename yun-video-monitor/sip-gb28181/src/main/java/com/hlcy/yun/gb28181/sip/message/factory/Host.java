package com.hlcy.yun.gb28181.sip.message.factory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Host {
    private String ip;
    private int port;

    Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    String toAddress() {
        return ip + ":" + port;
    }
}
