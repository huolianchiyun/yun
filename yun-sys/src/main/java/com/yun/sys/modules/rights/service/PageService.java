package com.yun.sys.modules.rights.service;

import com.yun.common.page.PageInfo;
import com.yun.common.mybatis.page.AbstractQueryPage;

import java.util.List;


public interface PageService<T extends AbstractQueryPage, R> {

    /**
     * 分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<R>> queryByCriteria(T criteria);

}
