package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import lombok.Data;

/**
 * 表 t_sys_role
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class RoleDO extends BaseDo implements ExcelSupport, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String roleName;

    /**
     * 角色等级
     */
    private Integer level;

    /**
     *
     */
    private String description;

    private Set<UserDO> users;

    protected Set<GroupDO> groups;

    protected Set<MenuDO> menus;

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("角色名称", roleName);
        map.put("角色级别", level);
        map.put("描述", description);
        map.put("创建日期", createTime);
        return map;
    }
}
