package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.datarights.NotPermission;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupApiRightsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface GroupApiRightsMapper {

    int insert(GroupApiRightsDO record);

    int batchInsert(@Param("groupMenus") Set<GroupApiRightsDO> groupMenus);

    @NotPermission
    int deleteByApiRightsIds(@Param("menuIds") Set<Long> menuIds);

    @NotPermission
    int deleteByGroupIds(@Param("groupIds")Set<Long> groupIds);
}
