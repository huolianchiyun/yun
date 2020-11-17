package com.yun.sys.modules.rights.model.vo;

import com.yun.sys.modules.rights.model.validation.ValidateFieldMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ValidateFieldMatch.List({
        @ValidateFieldMatch(first = "oldPassword", second = "newPassword", message = "新密码不能和旧密码相同", isEqualMode = false),
        @ValidateFieldMatch(first = "newPassword", second = "confirmPassword", message = "两次输入的密码必须相同")
})
public class UserPwdVO {
    @NotBlank(message = "用户名不能为空！")
    private String username;
    @NotBlank(message = "原密码不能为空！")
    private String oldPassword;
    @Pattern(regexp = "^.{6,18}$", message = "密码至少6位,最多18位！")
    @NotBlank(message = "新密码不能为空！")
    private String newPassword;
    @NotBlank(message = "确认密码不能为空！")
    private String confirmPassword;
}
