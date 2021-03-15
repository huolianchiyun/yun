package com.hlcy.yun.gb28181.service.sipmsg.flow;

public enum Operation {
    /**
     * Player operation
     */
    PLAY("Play"),
    PLAYBACK("Playback"),
    DOWNLOAD("Download"),

    /**
     * PTZ operate
     */
    RECORD("Record"), GUARD("Guard"), RESET_ALARM("ResetAlarm"), HOME_POSITION("HomePosition"), PRESET("Preset"),
    DEVICE_CONFIG("DeviceConfig"), CRUISE("Cruise"), AUXIL_SWITCH("AuxilSwitch"), FI("fi"), TELE_BOOT("TeleBoot"),
    IFRAME("Iframe"),

    /**
     * Query operate
     */
    CATALOG("Catalog"), DEVICE_INFO("DeviceInfo"), RECORD_INFO("RecordInfo"), CONFIG_DOWNLOAD("ConfigDownload"),

    /**
     * Subscribe
     */
    SUBSCRIBE("Subscribe"),

    /**
     * Notify operate
     */
    KEEPALIVE("Keepalive"), BROADCAST("Broadcast"), MEDIA_STATUS("MediaStatus"), ALARM("Alarm");

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
