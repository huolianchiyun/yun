package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserGroupDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupDOMapper {
    int insert(UserGroupDO record);

    int insertSelective(UserGroupDO record);
}
