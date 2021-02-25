package com.hlcy.yun.gb28181.service.sipmsg.flow;

public enum Operation {
    PLAY("Play"), PLAYBACK("Playback"), DOWNLOAD("Download"),
    RECORD("Record"), GUARD("Guard"), RESET_ALARM("ResetAlarm"), HOME_POSITION("HomePosition"), PRESET("Preset"),
    DEVICE_CONFIG("DeviceConfig"), CRUISE("Cruise"), AUXIL_SWITCH("AuxilSwitch"), FI("fi"), TELE_BOOT("TeleBoot"),
    IFRAME("Iframe"),
    CATALOG("Catalog"), DEVICE_INFO("DeviceInfo"), RECORD_INFO("RecordInfo"), ALARM("Alarm"),
    KEEPALIVE("Keepalive"), BROADCAST("Broadcast"), MEDIA_STATUS("MediaStatus");

    private String code;

    Operation(String code) {
        this.code = code;
    }

    public static Operation get(String code) {
        for (Operation value : Operation.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    public String code() {
        return code;
    }
}
