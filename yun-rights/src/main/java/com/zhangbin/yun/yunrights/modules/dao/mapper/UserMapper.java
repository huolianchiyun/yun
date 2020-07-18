package com.zhangbin.yun.yunrights.modules.dao.mapper;

import com.zhangbin.yun.yunrights.modules.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);
}