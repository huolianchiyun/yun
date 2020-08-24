package com.zhangbin.yun.yunrights.modules.logging.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.yunrights.modules.rights.datarights.NotPermission;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LogMapper extends PageMapper<LogDO> {

    @NotPermission
    int insert(LogDO record);

    LogDO selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int deleteByLogLevel(String logLevel);
}
