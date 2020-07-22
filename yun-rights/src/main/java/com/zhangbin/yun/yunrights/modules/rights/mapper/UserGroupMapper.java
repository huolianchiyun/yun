package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.UserGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupMapper {
    int insert(UserGroup record);

    int insertSelective(UserGroup record);
}