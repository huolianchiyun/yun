package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.enums.BizCodeEnum;
import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.email.model.Email;
import com.zhangbin.yun.yunrights.modules.email.service.EmailService;
import com.zhangbin.yun.yunrights.modules.rights.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yun/captcha")
@Api(tags = "系统：验证码管理")
public class CaptchaController {

    private final CaptchaService captchaService;
    private final EmailService emailService;

    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity<ResponseData> resetEmail(@RequestParam String email) {
        Email emailVo = captchaService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo);
        return success();
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<ResponseData> resetPassword(@RequestParam String email) {
        Email emailVo = captchaService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo);
        return success();
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<ResponseData> validate(@RequestParam String email, @RequestParam String code, @RequestParam Integer bizCode) {
        BizCodeEnum biEnum = BizCodeEnum.find(bizCode);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                captchaService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                captchaService.validate(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return success();
    }


}
