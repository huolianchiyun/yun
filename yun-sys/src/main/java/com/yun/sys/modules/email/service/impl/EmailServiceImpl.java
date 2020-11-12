package com.yun.sys.modules.email.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;

import com.yun.sys.modules.email.model.$do.EmailConfigDO;
import com.yun.sys.modules.email.model.Email;
import com.yun.common.exception.BadRequestException;
import com.yun.common.utils.encodec.EncryptUtils;
import com.yun.sys.modules.email.mapper.EmailConfigMapper;
import com.yun.sys.modules.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.Optional;

import static com.yun.common.constant.Constants.LEFT_ANGLE_BRACKET;
import static com.yun.common.constant.Constants.RIGHT_ANGLE_BRACKET;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "email")
public class EmailServiceImpl implements EmailService {

    private final EmailConfigMapper emailConfigMapper;

    @Override
    @CachePut(key = "'config'")
    public EmailConfigDO config(EmailConfigDO updatingEmailConfig) throws Exception {
        EmailConfigDO emailConfigDB = emailConfigMapper.selectByPrimaryKey(1L);
        if (!emailConfigDB.getPassword().equals(updatingEmailConfig.getPassword())) {
            // 对称加密
            updatingEmailConfig.setPassword(EncryptUtils.desEncrypt(updatingEmailConfig.getPassword()));
        }
        emailConfigDB.fresh(updatingEmailConfig);
        emailConfigMapper.updateByPrimaryKeySelective(emailConfigDB);
        return emailConfigDB;
    }

    @Override
    @Cacheable(key = "'config'")
    public EmailConfigDO queryEmailConfig() {
        return Optional.ofNullable(emailConfigMapper.selectByPrimaryKey(1L)).orElseGet(EmailConfigDO::new);
    }

    @Override
        public void send(Email email) {
        val emailConfig = emailConfigMapper.selectByPrimaryKey(1L);
        MailAccount account = createMailAccount(emailConfig);
        // 发送
        try {
            Mail.create(account)
                    .setTos(email.getRecipients().toArray(new String[email.getRecipients().size()]))
                    .setTitle(email.getSubject())
                    .setContent(email.getContent())
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private MailAccount createMailAccount(EmailConfigDO emailConfig) {
        Assert.isNull(emailConfig, "请先配置邮件信息，再操作");
        MailAccount account = new MailAccount();
        account.setUser(emailConfig.getUser());
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailConfig.getUser() + LEFT_ANGLE_BRACKET + emailConfig.getSender() + RIGHT_ANGLE_BRACKET);
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        return account;
    }
}
