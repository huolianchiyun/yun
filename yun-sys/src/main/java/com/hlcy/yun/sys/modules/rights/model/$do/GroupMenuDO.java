package com.hlcy.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_group_menu
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
public class GroupMenuDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long groupId;

    private Long menuId;

    public GroupMenuDO(Long roleId, Long menuId) {
        this.groupId = roleId;
        this.menuId = menuId;
    }
}
