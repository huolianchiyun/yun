package com.hlcy.yun.gb28181.service.sipmsg.flow;

public enum Operation {
    PLAY("Play"), PLAYBACK("Playback"),
    KEEPALIVE("Keepalive"),
    RECORD("record"), GUARD("guard"), RESET_ALARM("ResetAlarm"), HOME_POSITION("HomePosition"), PRESET("Preset"),
    CATALOG("Catalog"), DEVICE_INFO("DeviceInfo");

    private String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
