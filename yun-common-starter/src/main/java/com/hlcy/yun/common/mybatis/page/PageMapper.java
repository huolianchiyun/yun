package com.hlcy.yun.common.mybatis.page;

import com.hlcy.yun.common.model.BaseDO;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

public interface PageMapper<R extends BaseDO> {
   /**
    * 根据条件分页查询
    * @param criteria 条件
    * @return Set<R>
    */
   Set<R> selectByCriteria(@Param("criteria") AbstractQueryPage criteria);
}
