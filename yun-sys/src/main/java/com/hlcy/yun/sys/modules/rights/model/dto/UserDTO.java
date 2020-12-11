package com.hlcy.yun.sys.modules.rights.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.common.model.BaseDO;
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
