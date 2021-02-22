package com.hlcy.yun.gb28181.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter
@Setter
@ConfigurationProperties(prefix = "gb28181")
public class GB28181Properties implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ssrcFlowContextInRedisKey;

    private String sipIp;

    private Integer sipPort;

    private String sipDomain;

    private String sipId;

    private String sipPassword;

    private String mediaId;

    private String mediaIp;

    private Integer mediaVideoPort;

    private Integer mediaAudioPort;

    private String mediaMakeDevicePushStreamApi;

    private String mediaTestSsrcValidityApi;

    public void setSipIp(String sipIp) throws UnknownHostException {
        if (sipIp == null || sipIp.isEmpty()) {
            this.sipIp = InetAddress.getLocalHost().getHostAddress();
            return;
        }
        this.sipIp = sipIp;
    }
}
