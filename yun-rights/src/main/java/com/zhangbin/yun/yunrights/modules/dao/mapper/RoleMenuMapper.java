package com.zhangbin.yun.yunrights.modules.dao.mapper;

import com.zhangbin.yun.yunrights.modules.model.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMenuMapper {
    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);
}