package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.ApiRightsDO;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.ApiRightsQueryCriteria;
import java.util.List;

/**
 * API 访问权限
 * 提供 API 权限查询功能
 */
public interface ApiRightsService extends PageService<ApiRightsQueryCriteria, ApiRightsDO>{
    List<NameValue> queryAipGroup();

}
