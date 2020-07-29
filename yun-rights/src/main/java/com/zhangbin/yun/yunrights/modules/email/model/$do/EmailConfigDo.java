package com.zhangbin.yun.yunrights.modules.email.model.$do;

import java.io.Serializable;
import lombok.Data;

/**
 * 邮件配置表，数据覆盖式存入数据库
 * 表 t_email_config
 * @author ASUS
 * @date 2020-07-23 20:29:05
 */
@Data
public class EmailConfigDo implements Serializable {
    /**
     */
    private Long id;

    /**
     * 邮件服务器SMTP地址
     */
    private String host;

    /**
     * 邮件服务器SMTP端口
     */
    private String port;

    /**
     */
    private String password;

    /**
     */
    private String sender;

    /**
     * 收件人，多个以分号隔开
     */
    private String recipients;

    private static final long serialVersionUID = 1L;
}
