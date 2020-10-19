package com.zhangbin.yun.yunrights.modules.email.model.$do;

import com.zhangbin.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 邮件配置表，数据覆盖式存入数据库
 * 表 t_sys_email_config
 * @author ASUS
 * @date 2020-07-29 23:57:17
 */
@Getter
@Setter
public class EmailConfigDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 邮件服务器 SMTP 服务器域名
     */
    private String host;

    /**
     * 邮件服务器 SMTP 端口
     */
    private String port;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * 发送方，遵循RFC-822标准
     */
    private String sender;

    /**
     * 通过 other {@link EmailConfigDO} 刷新自己
     * @param other
     */
    public void fresh(EmailConfigDO other){
        if(StringUtils.hasText(other.host)){
            host = other.host;
        }
        if(StringUtils.hasText(other.port)){
            port = other.port;
        }
        if(StringUtils.hasText(other.password)){
            password = other.password;
        }
        if(StringUtils.hasText(other.user)){
            user = other.user;
        }
        if(StringUtils.hasText(other.sender)){
            sender = other.sender;
        }
    }

}
