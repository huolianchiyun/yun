package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.ApiRightsDO;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
public interface ApiRightsMapper extends PageMapper<ApiRightsDO> {
}
