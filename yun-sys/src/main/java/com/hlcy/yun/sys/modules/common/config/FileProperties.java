package com.hlcy.yun.sys.modules.common.config;

import com.hlcy.yun.common.constant.OSConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /** 文件大小限制 */
    private Long maxSize;

    /** 头像大小限制 */
    private Long avatarMaxSize;

    private YunPath mac;

    private YunPath linux;

    private YunPath windows;

    public YunPath getPath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(OSConstant.WIN)) {
            return windows;
        } else if(os.toLowerCase().startsWith(OSConstant.MAC)){
            return mac;
        }
        return linux;
    }

    @Data
    public static class YunPath {

        private String path;

        private String avatar;
    }
}
