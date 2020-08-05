package com.zhangbin.yun.yunrights.modules.common.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;

public final class PageQueryHelper {

    public static <R extends BaseDo> Page<R> queryAllByCriteriaWithPage(QueryPage criteria, PageMapper<R> mapper) {
        return PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize(), true)
                .doSelectPage(() -> mapper.selectAllByCriteria(criteria));
    }
}
