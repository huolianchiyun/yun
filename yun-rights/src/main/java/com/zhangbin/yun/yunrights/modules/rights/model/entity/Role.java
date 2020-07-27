package com.zhangbin.yun.yunrights.modules.rights.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_role
 * @author ASUS
 * @date 2020-07-27 21:58:22
 */
@Data
public class Role extends BaseEntity implements Serializable {
    /**
     */
    private String roleName;

    /**
     * 角色等级
     */
    private Integer level;

    /**
     */
    private String description;

    private static final long serialVersionUID = 1L;
}