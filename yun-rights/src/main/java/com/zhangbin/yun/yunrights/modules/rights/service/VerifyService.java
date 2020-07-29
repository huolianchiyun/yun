package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.email.model.Email;

public interface VerifyService {

    /**
     * 发送验证码
     * @param email /
     * @param key /
     * @return /
     */
    Email sendEmail(String email, String key);


    /**
     * 验证
     * @param code /
     * @param redisKey /
     */
    void validate(String redisKey, String code);

}
