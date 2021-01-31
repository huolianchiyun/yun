package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BroadcastResponse {
    private String ssrc;
    private String mediaIp;
    private String mediaSdp;

    public BroadcastResponse(String ssrc, String mediaIp, String mediaSdp) {
        this.ssrc = ssrc;
        this.mediaIp = mediaIp;
        this.mediaSdp = mediaSdp;
    }
}
