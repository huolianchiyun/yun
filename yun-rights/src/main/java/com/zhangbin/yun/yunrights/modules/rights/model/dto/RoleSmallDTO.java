package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleSmallDTO implements Serializable {

    private Long id;

    private String roleName;

    private Integer level;

    private String dataScope;
}
