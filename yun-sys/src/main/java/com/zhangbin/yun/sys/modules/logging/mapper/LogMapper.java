package com.zhangbin.yun.sys.modules.logging.mapper;

import com.zhangbin.yun.common.mybatis.page.PageMapper;
import com.zhangbin.yun.sys.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.sys.modules.rights.datarights.NotPermission;
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
