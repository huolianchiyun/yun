package com.yun.sys.modules.rights.service;

import com.yun.sys.modules.rights.model.$do.ApiRightsDO;
import com.yun.sys.modules.rights.model.common.NameValue;
import com.yun.sys.modules.rights.model.criteria.ApiRightsQueryCriteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * API 访问权限
 * 提供 API 权限查询功能
 */
public interface ApiRightsService extends PageService<ApiRightsQueryCriteria, ApiRightsDO>{
    // populate value in ApiRightsInit
    Set<NameValue> groups = new HashSet<>(5);
    List<NameValue> queryAipGroup();

}
