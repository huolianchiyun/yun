package com.zhangbin.yun.yunrights.modules.rights.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_role_menu
 * @author ASUS
 * @date 2020-07-27 21:58:22
 */
@Data
public class RoleMenu implements Serializable {
    /**
     */
    private Integer roleId;

    /**
     */
    private Integer menuId;

    private static final long serialVersionUID = 1L;
}