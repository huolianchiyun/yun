package com.zhangbin.yun.sys.modules.rights.mapper;

import com.zhangbin.yun.common.mybatis.page.PageMapper;
import com.zhangbin.yun.sys.modules.rights.model.$do.ApiRightsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface ApiRightsMapper extends PageMapper<ApiRightsDO> {

    Set<String> selectAuthorizationsByUserId(Long userId);
}
