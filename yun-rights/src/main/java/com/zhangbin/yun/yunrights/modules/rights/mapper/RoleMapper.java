package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface RoleMapper extends PageMapper<RoleDO> {

    RoleDO selectByPrimaryKey(Long id);

    Set<RoleDO> selectByPrimaryKeys(@Param("ids") Set<Long> ids);

    RoleDO selectByRoleName(String roleName);

    Set<RoleDO> selectByUserId(Long userId);

    Set<RoleDO> selectByMenuIds(@Param("menuIds") Set<Long> menuIds);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    int updateByPrimaryKeySelective(RoleDO record);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("roleIds") Set<Long> roleIds);

    int countAssociatedUserAndRole(@Param("roleIds") Set<Long> roleIds);

    /**
     * 查询用户集合中有多少个用户角色级别 >= 当前用户最大角色级别
     * @param levelOfCurrentUserMaxRole
     * @param userIds
     * @return
     */
    int countSuperLevelInUserIds(Integer levelOfCurrentUserMaxRole, @Param("userIds") Set<Long> userIds);
}
