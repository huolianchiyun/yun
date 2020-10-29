package com.zhangbin.yun.sys.modules.rights.mapper;

import com.zhangbin.yun.sys.modules.rights.datarights.NotPermission;
import com.zhangbin.yun.sys.modules.rights.model.$do.MenuApiRightsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface MenuApiRightsMapper {

    int insert(MenuApiRightsDO record);

    int batchInsert(@Param("menuApiRightsSet") Set<MenuApiRightsDO> menuApiRightsSet);

    @NotPermission
    int deleteByApiUrls(@Param("apiUrls") Set<String> apiUrls);

    @NotPermission
    int deleteByMenuIds(@Param("menuIds")Set<Long> menuIds);
}
