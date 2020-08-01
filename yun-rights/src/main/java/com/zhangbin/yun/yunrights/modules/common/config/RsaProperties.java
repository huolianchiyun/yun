package com.zhangbin.yun.yunrights.modules.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @website https://el-admin.vip
 **/
@Data
@Component
public class RsaProperties {

    public static String privateKey;

    @Value("${rsa.private_key}")
    public void setPrivateKey(String privateKey) {
        RsaProperties.privateKey = privateKey;
    }
}
