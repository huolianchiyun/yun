package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayResponse {
    private String ssrc;
    private String mediaIp;

    public PlayResponse(String ssrc, String mediaIp) {
        this.ssrc = ssrc;
        this.mediaIp = mediaIp;
    }
}
