package com.hlcy.yun.sys.modules.security.controller;

import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousDeleteMapping;
import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousPostMapping;
import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.common.spring.security.SecurityUtils;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import com.hlcy.yun.sys.modules.security.service.LoginAuthService;
import com.hlcy.yun.sys.modules.security.model.dto.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import static com.hlcy.yun.common.web.response.ResponseUtil.success;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/yun/auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权")
public class LoginAuthController {
    private final LoginAuthService loginAuthService;

    @Logging("用户登录")
    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<ResponseData<Map<String, Object>>> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request) throws Exception {
        return success(loginAuthService.login(authUser, request));
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<ResponseData<UserDetails>> getUserInfo() {
        return success(SecurityUtils.getCurrentUser());
    }

    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<ResponseData<Map<String, Object>>> getCode() {
        return success(loginAuthService.getCaptcha());
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<ResponseData<Void>> logout(HttpServletRequest request) {
        loginAuthService.logout(request);
        return success();
    }

    @ApiOperation("第三方服务token委托验证接口")
    @AnonymousGetMapping(value = "/token")
    public ResponseEntity<ResponseData<Void>> checkToken() {
        return success();
    }
}
