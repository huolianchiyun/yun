package com.zhangbin.yun.sys.modules.email.controller;

import com.zhangbin.yun.common.web.response.ResponseData;

import static com.zhangbin.yun.common.web.response.ResponseUtil.success;

import com.zhangbin.yun.sys.modules.email.model.$do.EmailConfigDO;
import com.zhangbin.yun.sys.modules.email.model.Email;
import com.zhangbin.yun.sys.modules.email.service.EmailService;
import com.zhangbin.yun.sys.modules.logging.annotation.Logging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/yun/email")
@Api(tags = "系统：邮件管理")
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<ResponseData<EmailConfigDO>> queryConfig() {
        return success(emailService.queryEmailConfig());
    }

    @Logging("配置邮件")
    @PutMapping
    @ApiOperation("配置邮件")
    public ResponseEntity<ResponseData<Void>> updateConfig(@RequestBody EmailConfigDO emailConfig) throws Exception {
        emailService.config(emailConfig);
        return success();
    }

    @Logging("发送邮件")
    @PostMapping
    @ApiOperation("发送邮件")
    public ResponseEntity<ResponseData<Void>> sendEmail(@RequestBody Email email) {
        emailService.send(email);
        return success();
    }
}
