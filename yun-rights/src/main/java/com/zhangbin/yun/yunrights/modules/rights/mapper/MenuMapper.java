package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.*;
import java.util.Set;

@Mapper
public interface MenuMapper extends PageMapper<MenuDO> {
    MenuDO selectByPrimaryKey(Long id);

    Set<MenuDO> selectByPid(Long pid);

    Set<MenuDO> batchSelectById(@Param("menuIds")Set<Long> menuIds);

    Set<MenuDO> selectByRoleIds(@Param("roleIds") Set<Long> roleIds);

    int insert(MenuDO record);

    int updateByPrimaryKeySelective(MenuDO record);

    int deleteByPrimaryKey(Long id);

    int batchDeleteByIds(@Param("menuIds") Set<Long> menuIds);
}
