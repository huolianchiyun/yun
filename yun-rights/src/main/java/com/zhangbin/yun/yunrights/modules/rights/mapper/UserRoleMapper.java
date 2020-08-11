package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface UserRoleMapper {

    int insert(UserRoleDO record);

    int deleteByUserIds(@Param("userIds") Set<Long> userIds);
}
