package com.zhangbin.yun.yunrights.modules.email.service;


import com.zhangbin.yun.yunrights.modules.email.model.Email;
import com.zhangbin.yun.yunrights.modules.email.model.$do.EmailConfigDo;

public interface EmailService {

    /**
     * 更新邮件配置
     * @param emailConfigDo 邮箱配置
     * @param old /
     * @return /
     * @throws Exception /
     */
    EmailConfigDo config(EmailConfigDo emailConfigDo, EmailConfigDo old) throws Exception;

    /**
     * 查询配置
     * @return EmailConfig 邮件配置
     */
    EmailConfigDo find();

    /**
     * 发送邮件
     * @param emailVo 邮件发送的内容
     * @param emailConfigDo 邮件配置
     */
    void send(Email emailVo, EmailConfigDo emailConfigDo);
}
