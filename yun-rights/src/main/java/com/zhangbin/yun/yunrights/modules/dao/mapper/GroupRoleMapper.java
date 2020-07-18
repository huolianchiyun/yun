package com.zhangbin.yun.yunrights.modules.dao.mapper;

import com.zhangbin.yun.yunrights.modules.model.entity.GroupRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupRoleMapper {
    int insert(GroupRole record);

    int insertSelective(GroupRole record);
}