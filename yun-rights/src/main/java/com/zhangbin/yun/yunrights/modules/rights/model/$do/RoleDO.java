package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import java.io.Serializable;
import java.util.Set;

import lombok.Data;

/**
 * 表 t_sys_role
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class RoleDO extends BaseDo implements Serializable {

    private static final long serialVersionUID = 1L;
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

    private Set<UserDO> users;

    protected Set<GroupDO> groups;

    protected Set<MenuDO> menus;
}
