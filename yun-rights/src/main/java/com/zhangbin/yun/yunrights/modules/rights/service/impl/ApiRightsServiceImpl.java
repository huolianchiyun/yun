package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import com.github.pagehelper.Page;
import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.mybatis.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.ApiRightsMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.ApiRightsDO;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.ApiRightsQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.ApiRightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiRightsServiceImpl implements ApiRightsService {
    // populate value in ApiRightsInit
    public final static Set<NameValue> groups = new HashSet<>(5);
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
