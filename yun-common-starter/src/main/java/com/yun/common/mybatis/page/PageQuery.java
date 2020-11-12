package com.yun.common.mybatis.page;

import com.github.pagehelper.Page;
import com.yun.common.model.BaseDO;

public abstract class PageQuery<T extends AbstractQueryPage, R extends BaseDO> {
   private T queryPage;
   private PageMapper<R> mapper;

   protected PageQuery(T queryPage, PageMapper<R> mapper) {
      this.queryPage = queryPage;
      this.mapper = mapper;
   }

   public abstract Page<R> queryByCriteriaWithPage(AbstractQueryPage queryPage, PageMapper<R> mapper);

   public T getQueryPage() {
      return queryPage;
   }

   public PageMapper<R> getMapper() {
      return mapper;
   }
}
