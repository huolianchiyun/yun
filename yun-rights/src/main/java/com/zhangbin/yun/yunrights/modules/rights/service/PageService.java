package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;

import java.util.List;


public interface PageService<T extends AbstractQueryPage, R> {

    /**
     * 分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<R>> queryAllByCriteria(T criteria);

}
