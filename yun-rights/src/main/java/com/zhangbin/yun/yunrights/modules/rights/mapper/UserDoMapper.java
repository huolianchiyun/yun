package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);
}