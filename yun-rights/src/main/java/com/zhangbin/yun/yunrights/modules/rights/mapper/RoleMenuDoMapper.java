package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleMenuDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMenuDoMapper {
    int insert(RoleMenuDo record);

    int insertSelective(RoleMenuDo record);
}