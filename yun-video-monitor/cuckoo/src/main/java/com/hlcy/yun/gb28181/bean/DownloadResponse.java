package com.hlcy.yun.gb28181.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DownloadResponse {
    private String ssrc;
    private String mediaIp;
    private long fileSize;

    public DownloadResponse(String ssrc, String mediaIp, long fileSize) {
        this.ssrc = ssrc;
        this.mediaIp = mediaIp;
        this.fileSize = fileSize;
    }
}
