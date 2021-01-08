package com.hlcy.yun.gb28181.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter
@Setter
@ConfigurationProperties(prefix = "gb28181")
public class GB28181Properties {

    private String sipIp;

    private Integer sipPort;

    private String sipDomain;

    private String sipId;

    private String sipPassword;

    private String mediaId;

    private String mediaIp;

    private Integer mediaPort;

    private Integer ptzSpeed;

    public void setSipIp(String sipIp) throws UnknownHostException {
        if (sipIp == null || sipIp.isEmpty()) {
            this.sipIp = InetAddress.getLocalHost().getHostAddress();
            return;
        }
        this.sipIp = sipIp;
    }
}
