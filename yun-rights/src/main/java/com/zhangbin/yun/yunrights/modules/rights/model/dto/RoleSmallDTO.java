package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleSmallDTO implements Serializable {

    private Long id;

    private String roleName;

    private Integer level;

    private String dataScope;
}
