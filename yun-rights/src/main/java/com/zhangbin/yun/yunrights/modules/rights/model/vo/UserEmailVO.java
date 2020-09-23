package com.zhangbin.yun.yunrights.modules.rights.model.vo;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserEmailVO {
    @NotBlank(groups = BaseDO.Update.class, message = "验证码不能为空！")
    private String captcha;
    @NotBlank(groups = BaseDO.Update.class, message = "username 不能为空！")
    private String  username;
    @NotBlank(groups = BaseDO.Update.class, message = "用户密码不能为空！")
    private String  password;
    @NotBlank(groups = BaseDO.Update.class, message = "邮箱不能为空！")
    @Email
    private String email;
}
