package com.yun.sys.modules.rights.mapper;

import com.yun.sys.modules.rights.model.$do.ApiRightsDO;
import com.yun.common.mybatis.page.PageMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface ApiRightsMapper extends PageMapper<ApiRightsDO> {

    Set<String> selectAuthorizationsByUserId(Long userId);

    Set<ApiRightsDO> selectAuthorizationsByMenuId(Long menuId);
}
