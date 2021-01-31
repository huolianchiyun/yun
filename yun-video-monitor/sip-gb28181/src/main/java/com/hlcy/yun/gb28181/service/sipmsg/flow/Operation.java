package com.hlcy.yun.gb28181.service.sipmsg.flow;

public enum Operation {
    PLAY("Play"), PLAYBACK("Playback"),
    RECORD("record"), GUARD("guard"), RESET_ALARM("ResetAlarm"), HOME_POSITION("HomePosition"), PRESET("Preset"),
    CATALOG("Catalog"), DEVICE_INFO("DeviceInfo"),
    KEEPALIVE("Keepalive"), BROADCAST("Broadcast");

    private String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
