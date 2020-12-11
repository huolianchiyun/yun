package com.hlcy.yun.sys.modules.rights.mapper;

import com.hlcy.yun.sys.modules.rights.model.$do.ApiRightsDO;
import com.hlcy.yun.common.mybatis.page.PageMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface ApiRightsMapper extends PageMapper<ApiRightsDO> {

    Set<String> selectAuthorizationsByUserId(Long userId);

    Set<ApiRightsDO> selectAuthorizationsByMenuId(Long menuId);
}
