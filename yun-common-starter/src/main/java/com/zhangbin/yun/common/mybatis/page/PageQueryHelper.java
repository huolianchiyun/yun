package com.zhangbin.yun.common.mybatis.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhangbin.yun.common.model.BaseDO;

public final class PageQueryHelper {

    public static <R extends BaseDO> Page<R> queryByCriteriaWithPage(AbstractQueryPage criteria, PageMapper<R> mapper) {
        prepareCriteria(criteria);
        return PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize(), true)
                .doSelectPage(() -> mapper.selectByCriteria(criteria));
    }

    /**
     * 预处理 criteria， 主要检测 pageNum、pageSize 合法性， 若不合法，则修正为合法值
     *
     * @param criteria
     */
    private static void prepareCriteria(AbstractQueryPage criteria) {
        if (criteria.getPageNum() < 0) criteria.setPageNum(1);
        if (criteria.getPageSize() <= 0) criteria.setPageSize(10);
    }
}
