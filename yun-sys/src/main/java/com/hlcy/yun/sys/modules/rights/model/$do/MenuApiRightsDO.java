package com.hlcy.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 组和API权限关联表
 * 表 t_sys_group_api_rights
 * @author ASUS
 * @date 2020-10-12 21:25:54
 */
@Getter
@Setter
@NoArgsConstructor
public class MenuApiRightsDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * ApiRightsId
     */
    private String apiUrl;

    public MenuApiRightsDO(Long menuId, String apiUrl) {
        this.menuId = menuId;
        this.apiUrl = apiUrl;
    }
}
