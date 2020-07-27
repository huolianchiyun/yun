package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.entity.GroupRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupRoleMapper {
    int insert(GroupRole record);

    int insertSelective(GroupRole record);
}