package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_role_menu
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class RoleMenuDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     */
    private Long roleId;

    /**
//     */
    private Long menuId;

    public RoleMenuDO(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
