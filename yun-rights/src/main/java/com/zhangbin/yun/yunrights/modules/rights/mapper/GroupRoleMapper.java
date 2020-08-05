package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupRoleDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupRoleMapper {
    int insert(GroupRoleDO record);

    int insertSelective(GroupRoleDO record);
}
