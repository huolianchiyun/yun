package com.zhangbin.yun.yunrights.modules.security.controller;

import com.zhangbin.yun.yunrights.modules.common.annotation.rest.AnonymousDeleteMapping;
import com.zhangbin.yun.yunrights.modules.common.annotation.rest.AnonymousGetMapping;
import com.zhangbin.yun.yunrights.modules.common.annotation.rest.AnonymousPostMapping;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.security.service.LoginAuthService;
import com.zhangbin.yun.yunrights.modules.security.service.dto.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权接口")
public class LoginAuthController {
    private final LoginAuthService loginAuthService;

    @Logging("用户登录")
    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<ResponseData> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request) throws Exception {
        return success(loginAuthService.login(authUser, request));
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<ResponseData> getUserInfo() {
        return success(SecurityUtils.getCurrentUser());
    }

    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<ResponseData> getCode() {
        return success(loginAuthService.getCode());
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<ResponseData> logout(HttpServletRequest request) {
        loginAuthService.logout(request);
        return success();
    }
}
