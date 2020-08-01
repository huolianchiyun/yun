package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
public class UserDto extends BaseDo implements Serializable {


    private Set<RoleSmallDto> roles;

    private DeptSmallDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin = false;

    private Date pwdResetTime;
}
