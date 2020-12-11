package com.hlcy.yun.sys.modules.rights.service.impl;

import com.github.pagehelper.Page;
import com.hlcy.yun.sys.modules.rights.model.$do.ApiRightsDO;
import com.hlcy.yun.sys.modules.rights.model.common.NameValue;
import com.hlcy.yun.sys.modules.rights.service.ApiRightsService;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.mybatis.page.PageQueryHelper;
import com.hlcy.yun.sys.modules.rights.mapper.ApiRightsMapper;
import com.hlcy.yun.sys.modules.rights.model.criteria.ApiRightsQueryCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
class ApiRightsServiceImpl implements ApiRightsService {

    private final ApiRightsMapper apiRightsMapper;

    @Override
    public List<NameValue> queryAipGroup() {
        return new ArrayList<>(groups);
    }

    @Override
    public PageInfo<List<ApiRightsDO>> queryByCriteria(ApiRightsQueryCriteria criteria) {
        Page<ApiRightsDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, apiRightsMapper);
        PageInfo<List<ApiRightsDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<ApiRightsDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }
}
