package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.datarights.NotPermission;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface GroupMenuMapper {

    int insert(GroupMenuDO record);

    int batchInsert(@Param("groupMenus") Set<GroupMenuDO> groupMenus);

    @NotPermission
    int deleteByMenuIds(@Param("menuIds") Set<Long> menuIds);

    @NotPermission
    int deleteByGroupIds(@Param("groupIds")Set<Long> groupIds);
}
