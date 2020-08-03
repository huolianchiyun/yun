package com.zhangbin.yun.yunrights.modules.common.page;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.logging.model.dto.QueryPage;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PageMapper<R extends BaseDo> {
   List<R> selectAllByCriteria(@Param("criteria") QueryPage criteria);
}
