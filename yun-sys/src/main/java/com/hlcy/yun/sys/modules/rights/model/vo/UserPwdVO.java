package com.hlcy.yun.sys.modules.rights.model.vo;

import com.hlcy.yun.sys.modules.rights.model.validation.ValidateFieldMatch;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ValidateFieldMatch.List({
        @ValidateFieldMatch(first = "oldPwd", second = "newPwd", message = "新密码不能和旧密码相同", isEqualMode = false, isDecrypt = true),
        @ValidateFieldMatch(first = "newPwd", second = "confirmPwd", message = "两次输入的密码必须相同", isDecrypt = true)
})
public class UserPwdVO {
    @NotBlank(message = "用户名不能为空！")
    private String username;
    @NotBlank(message = "原密码不能为空！")
    private String oldPwd;
    @NotBlank(message = "新密码不能为空！")
    private String newPwd;
    @NotBlank(message = "确认密码不能为空！")
    private String confirmPwd;
}
