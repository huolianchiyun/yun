package com.yun.sys.modules.rights.mapper;

import com.yun.sys.modules.rights.datarights.NotPermission;
import com.yun.sys.modules.rights.model.$do.UserGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface UserGroupMapper {

    int insert(UserGroupDO record);

    void batchInsert(@Param("userGroups") Set<UserGroupDO> userGroups);

    @NotPermission
    int deleteByGroupIds(@Param("groupIds") Set<Long> groupIds);

    @NotPermission
    int deleteByUserIds(@Param("userIds") Set<Long> userIds);
}
