package com.hlcy.yun.gb28181.sip.message.factory;

public enum Transport {
    TCP("TCP"), UDP("UDP");
    private String name;

    Transport(String name) {
        this.name = name;
    }
}
