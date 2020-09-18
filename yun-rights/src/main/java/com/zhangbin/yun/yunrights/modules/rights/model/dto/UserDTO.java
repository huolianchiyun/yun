package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserDTO extends BaseDO implements Serializable {

    private String nickName;

    private String username;

    private String gender;

    @JsonIgnore
    private String password;

    private String phone;

    private String email;

    private Long deptId;

    @JsonIgnore
    private Boolean admin;

    private Boolean status;

    private Boolean deleted;

    private Set<GroupDO> groups;

    private DeptSmallDTO dept;

    private Date pwdResetTime;

}
