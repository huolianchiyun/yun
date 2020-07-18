package com.zhangbin.yun.yunrights.modules.dao.mapper;

import com.zhangbin.yun.yunrights.modules.model.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}