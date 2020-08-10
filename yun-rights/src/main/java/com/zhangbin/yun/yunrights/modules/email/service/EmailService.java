package com.zhangbin.yun.yunrights.modules.email.service;


import com.zhangbin.yun.yunrights.modules.email.model.$do.EmailConfigDO;
import com.zhangbin.yun.yunrights.modules.email.model.Email;

public interface EmailService {

    /**
     * 更新邮件配置
     * @param updatingEmailConfig 邮箱配置
     * @return  {@link EmailConfigDO}
     * @throws Exception /
     */
    EmailConfigDO config(EmailConfigDO updatingEmailConfig) throws Exception;

    /**
     * 查询邮件配置
     * @return  {@link EmailConfigDO}
     */
    EmailConfigDO queryEmailConfig();

    /**
     * 发送邮件
     * @param email 邮件
     */
    void send(Email email);
}
