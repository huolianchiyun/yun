package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserGroupDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupDoMapper {
    int insert(UserGroupDo record);

    int insertSelective(UserGroupDo record);
}