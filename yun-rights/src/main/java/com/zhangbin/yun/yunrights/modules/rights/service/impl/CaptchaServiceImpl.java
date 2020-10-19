package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.zhangbin.yun.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.utils.RedisUtils;
import com.zhangbin.yun.yunrights.modules.email.model.Email;
import com.zhangbin.yun.yunrights.modules.rights.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${code.expiration}")
    private Long expiration;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Email sendEmail(String email, String key) {
        Email emailVo;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        Object oldCode =  redisUtils.get(redisKey);
        if(oldCode == null){
            String code = RandomUtil.randomNumbers (6);
            // 存入缓存
            Assert.isTrue(redisUtils.set(redisKey, code, expiration), "服务异常，请联系网站负责人");
            content = template.render(Dict.create().set("code",code));
            emailVo = new Email(Collections.singletonList(email),"yunrights",content);
        // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code",oldCode));
            emailVo = new Email(Collections.singletonList(email),"yunrights",content);
        }
        return emailVo;
    }


    public void validate(String redisKey, String code) {
        Object value = redisUtils.get(redisKey);
        if(value == null || !value.toString().equals(code)){
            throw new BadRequestException("无效验证码");
        } else {
            redisUtils.del(redisKey);
        }
    }
}
