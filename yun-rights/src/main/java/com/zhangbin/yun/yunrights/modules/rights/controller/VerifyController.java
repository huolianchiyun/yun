//package com.zhangbin.yun.yunrights.modules.rights.controller;
//
//import com.zhangbin.yun.yunrights.modules.common.enums.BizCodeEnum;
//import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
//import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
//import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
//import com.zhangbin.yun.yunrights.modules.email.model.Email;
//import com.zhangbin.yun.yunrights.modules.email.service.EmailService;
//import com.zhangbin.yun.yunrights.modules.rights.service.VerifyService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.Objects;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/code")
//@Api(tags = "系统：验证码管理")
//public class VerifyController {
//
//    private final VerifyService verifyService;
//    private final EmailService emailService;
//
//    @PostMapping(value = "/resetEmail")
//    @ApiOperation("重置邮箱，发送验证码")
//    public ResponseEntity<ResponseData> resetEmail(@RequestParam String email) {
//        Email emailVo = verifyService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
//        emailService.send(emailVo, emailService.find());
//        return success();
//    }
//
//    @PostMapping(value = "/email/resetPass")
//    @ApiOperation("重置密码，发送验证码")
//    public ResponseEntity<ResponseData> resetPass(@RequestParam String email) {
//        Email emailVo = verifyService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
//        emailService.send(emailVo, emailService.find());
//        return success();
//    }
//
//    @GetMapping(value = "/validated")
//    @ApiOperation("验证码验证")
//    public ResponseEntity<ResponseData> validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer bizCode) {
//        BizCodeEnum biEnum = BizCodeEnum.find(bizCode);
//        switch (Objects.requireNonNull(biEnum)) {
//            case ONE:
//                verifyService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
//                break;
//            case TWO:
//                verifyService.validate(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
//                break;
//            default:
//                break;
//        }
//        return success();
//    }
//
//
//}
