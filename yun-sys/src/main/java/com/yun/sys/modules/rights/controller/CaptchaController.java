package com.yun.sys.modules.rights.controller;

import com.yun.sys.modules.common.enums.BizCodeEnum;
import com.yun.sys.modules.common.enums.CodeEnum;
import com.yun.sys.modules.email.model.Email;
import com.yun.sys.modules.rights.service.CaptchaService;

import static com.yun.common.web.response.ResponseUtil.success;
import com.yun.common.web.response.ResponseData;
import com.yun.sys.modules.email.service.EmailService;
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
    public ResponseEntity<ResponseData<Void>> resetEmail(@RequestParam String email) {
        Email emailVo = captchaService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo);
        return success();
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<ResponseData<Void>> resetPassword(@RequestParam String email) {
        Email emailVo = captchaService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo);
        return success();
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<ResponseData<Void>> validate(@RequestParam String email, @RequestParam String code, @RequestParam Integer bizCode) {
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
