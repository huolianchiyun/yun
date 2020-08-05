package com.zhangbin.yun.yunrights.modules.logging.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LogMapper extends PageMapper<LogDO> {

    int insert(LogDO record);

    LogDO selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int deleteByLogLevel(String logLevel);
}
