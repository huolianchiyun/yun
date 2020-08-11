package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface GroupRoleMapper {
    int insert(GroupRoleDO record);

    int deleteByGroupIds(@Param("groupIds") Set<Long> groupIds);
}
