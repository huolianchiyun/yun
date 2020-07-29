package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupRoleDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupRoleDoMapper {
    int insert(GroupRoleDo record);

    int insertSelective(GroupRoleDo record);
}