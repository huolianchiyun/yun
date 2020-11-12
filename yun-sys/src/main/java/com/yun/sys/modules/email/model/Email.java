package com.yun.sys.modules.email.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送邮件时，接收参数的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    /** 收件人，支持多个收件人 */
    @NotEmpty
    private List<String> recipients;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
