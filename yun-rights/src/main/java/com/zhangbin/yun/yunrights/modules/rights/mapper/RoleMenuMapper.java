package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface RoleMenuMapper {
    int insert(RoleMenuDO record);

    int batchInsert(@Param("roleMenus") Set<RoleMenuDO> roleMenus);

    int batchDeleteByRoleId(Long roleId);
}
