package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface UserGroupMapper {

    int insert(UserGroupDO record);

    void addUsersIntoGroup(@Param("userIds") Set<Long> userIds, Long groupId);

    int deleteByGroupIds(@Param("groupIds") Set<Long> groupIds);

    int deleteByUserIds(@Param("userIds") Set<Long> userIds);
}
