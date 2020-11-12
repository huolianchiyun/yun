package com.yun.sys.modules.logging.mapper;

import com.yun.sys.modules.rights.datarights.NotPermission;
import com.yun.common.mybatis.page.PageMapper;
import com.yun.sys.modules.logging.model.$do.LogDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
@NotPermission
public interface LogMapper extends PageMapper<LogDO> {

    @NotPermission
    int insert(LogDO record);

    LogDO selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int deleteByLogLevel(String logLevel);
}
