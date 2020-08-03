package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface RoleDoMapper {
    RoleDo selectByPrimaryKey(Long id);

    Set<RoleDo> selectRoleByUserId(Long userId);

    int deleteByPrimaryKey(Long id);

    int insert(RoleDo record);

    int insertSelective(RoleDo record);

    int updateByPrimaryKeySelective(RoleDo record);

    int updateByPrimaryKey(RoleDo record);
}
