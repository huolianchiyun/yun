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

    List<RoleDO> selectByPrimaryKeys(@Param("ids") Set<Long> ids);

    RoleDO selectByRoleName(String roleName);

    List<RoleDO> selectRoleByUserId(Long userId);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    int updateByPrimaryKeySelective(RoleDO record);

    int deleteByPrimaryKey(Long id);

    int batchDeleteByIds(@Param("roleIds") Set<Long> ids);

    int countAssociatedUsers(Set<Long> ids);

    int countSuperLevelInUserIds(Integer levelOfCurrentUserMaxRole, @Param("userIds") Set<Long> userIds);
}
