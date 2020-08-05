package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int insert(UserRoleDO record);

    int insertSelective(UserRoleDO record);
}
