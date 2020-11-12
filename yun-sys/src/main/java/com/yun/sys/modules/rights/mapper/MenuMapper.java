package com.yun.sys.modules.rights.mapper;

import com.yun.common.mybatis.page.PageMapper;
import com.yun.sys.modules.rights.model.$do.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface MenuMapper extends PageMapper<MenuDO> {
    MenuDO selectByPrimaryKey(Long id);

    Set<MenuDO> selectByPid(Long pid);

    Set<MenuDO> batchSelectById(@Param("menuIds")Set<Long> menuIds);

    Set<MenuDO> selectByGroupIds(@Param("groupIds") Set<Long> groupIds);

    Set<MenuDO> selectByGroupId(Long groupId);

    Set<MenuDO> selectRouterMenusByUserId(Long userId);

    /**
     * 为管理员查询路由菜单
     * @return /
     */
    Set<MenuDO> selectRouterMenus();

    int insert(MenuDO record);

    int updateByPrimaryKeySelective(MenuDO record);

    int deleteByPrimaryKey(Long id);

    int batchDeleteByIds(@Param("menuIds") Set<Long> menuIds);
}