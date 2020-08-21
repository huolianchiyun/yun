package com.zhangbin.yun.yunrights.modules.security.service;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.zhangbin.yun.yunrights.modules.common.utils.RedisUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.RsaUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.StringUtils;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.security.config.bean.LoginProperties;
import com.zhangbin.yun.yunrights.modules.security.config.bean.SecurityProperties;
import com.zhangbin.yun.yunrights.modules.security.security.TokenProvider;
import com.zhangbin.yun.yunrights.modules.security.model.dto.AuthUser;
import com.zhangbin.yun.yunrights.modules.security.model.dto.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class LoginAuthService {
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginProperties loginProperties;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public Map<String, Object> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request) throws Exception {
        validateCaptcha(authUser);
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        // 密码正确验证:UserDetailsServiceImpl#loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(myUserDetails, token, request);
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        // 返回 token 与 用户信息
        return new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", myUserDetails);
        }};
    }

    public void logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
    }

    public Map<String, Object> getCaptcha() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, captcha.text(), loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        return new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
    }

    private void validateCaptcha(AuthUser authUser) {
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        Assert.isTrue(StringUtils.isNotBlank(code)
                && StringUtils.isNotBlank(authUser.getCode())
                && authUser.getCode().equalsIgnoreCase(code), "验证码不存在或已过期！");
    }
}
