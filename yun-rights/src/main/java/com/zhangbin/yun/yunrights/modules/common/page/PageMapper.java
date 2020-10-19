package com.zhangbin.yun.yunrights.modules.common.page;

import com.zhangbin.yun.common.model.BaseDO;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

public interface PageMapper<R extends BaseDO> {
   /**
    * 根据条件分页查询
    * @param criteria 条件
    * @return Set<R>
    */
   Set<R> selectAllByCriteria(@Param("criteria") AbstractQueryPage criteria);
}
