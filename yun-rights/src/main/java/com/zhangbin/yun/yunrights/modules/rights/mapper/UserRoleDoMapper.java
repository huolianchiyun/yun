package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserRoleDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDoMapper {
    int insert(UserRoleDo record);

    int insertSelective(UserRoleDo record);
}