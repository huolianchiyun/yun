package com.zhangbin.yun.yunrights.modules.dao.mapper;

import com.zhangbin.yun.yunrights.modules.model.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupMapper {
    int insert(UserGroup record);

    int insertSelective(UserGroup record);
}