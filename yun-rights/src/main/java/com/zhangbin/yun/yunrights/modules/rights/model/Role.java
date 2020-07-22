package com.zhangbin.yun.yunrights.modules.rights.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_role
 * @author ASUS
 * @date 2020-07-21 21:51:01
 */
@Data
public class Role extends BaseEntity implements Serializable {
    /**
     */
    private String roleName;

    /**
     */
    private String description;

    /**
     */
    private Boolean level;

    private static final long serialVersionUID = 1L;
}