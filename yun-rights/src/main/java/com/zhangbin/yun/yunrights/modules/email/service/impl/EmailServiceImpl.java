//package com.zhangbin.yun.yunrights.modules.email.service.impl;
//
//import cn.hutool.extra.mail.Mail;
//import cn.hutool.extra.mail.MailAccount;
//import com.zhangbin.yun.yunrights.common.exception.BadRequestException;
//import com.zhangbin.yun.yunrights.common.utils.EncryptUtils;
//import com.zhangbin.yun.yunrights.modules.email.service.EmailService;
//import com.zhangbin.yun.yunrights.modules.rights.model.Email;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@CacheConfig(cacheNames = "email")
//public class EmailServiceImpl implements EmailService {
//
//    private final EmailMapper emailMapper;
//
//    @Override
//    @CachePut(key = "'config'")
//    @Transactional(rollbackFor = Exception.class)
//    public EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception {
//        emailConfig.setId(1L);
//        if(!emailConfig.getPass().equals(old.getPass())){
//            // 对称加密
//            emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
//        }
//        return emailMapper.save(emailConfig);
//    }
//
//    @Override
//    @Cacheable(key = "'config'")
//    public EmailConfig find() {
//        Optional<EmailConfig> emailConfig = emailMapper.findById(1L);
//        return emailConfig.orElseGet(EmailConfig::new);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void send(Email email, EmailConfig emailConfig){
//        if(emailConfig == null){
//            throw new BadRequestException("请先配置，再操作");
//        }
//        // 封装
//        MailAccount account = new MailAccount();
//        account.setUser(emailConfig.getUser());
//        account.setHost(emailConfig.getHost());
//        account.setPort(Integer.parseInt(emailConfig.getPort()));
//        account.setAuth(true);
//        try {
//            // 对称解密
//            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
//        } catch (Exception e) {
//            throw new BadRequestException(e.getMessage());
//        }
//        account.setFrom(emailConfig.getUser()+"<"+emailConfig.getFromUser()+">");
//        // ssl方式发送
//        account.setSslEnable(true);
//        // 使用STARTTLS安全连接
//        account.setStarttlsEnable(true);
//        String content = email.getContent();
//        // 发送
//        try {
//            int size = email.getReceivers().size();
//            Mail.create(account)
//                    .setTos(email.getReceivers().toArray(new String[size]))
//                    .setTitle(email.getSubject())
//                    .setContent(content)
//                    .setHtml(true)
//                    //关闭session
//                    .setUseGlobalSession(false)
//                    .send();
//        }catch (Exception e){
//            throw new BadRequestException(e.getMessage());
//        }
//    }
//}
