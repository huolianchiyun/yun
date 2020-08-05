package com.zhangbin.yun.yunrights.modules.email.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import java.io.Serializable;
import lombok.Data;

/**
 * 邮件配置表，数据覆盖式存入数据库
 * 表 t_sys_email_config
 * @author ASUS
 * @date 2020-07-29 23:57:17
 */
@Data
public class EmailConfigDO extends BaseDo implements Serializable {
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
