package com.yun.sys.modules.rights.model.vo;

import com.yun.common.model.BaseDO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserPwdVO {
    @NotBlank(groups = BaseDO.Update.class, message = "username 不能为空！")
    private String  username;
    @NotBlank(groups = BaseDO.Update.class, message = "oldPass 不能为空！")
    private String oldPass;
    @NotBlank(groups = BaseDO.Update.class, message = "newPass 不能为空！")
    private String newPass;
}
