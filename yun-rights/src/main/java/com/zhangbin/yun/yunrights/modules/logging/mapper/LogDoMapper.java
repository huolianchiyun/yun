package com.zhangbin.yun.yunrights.modules.logging.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.logging.model.$do.LogDo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LogDoMapper extends PageMapper<LogDo> {

    int insert(LogDo record);

    int insertSelective(LogDo record);

    LogDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogDo record);

    int updateByPrimaryKey(LogDo record);

    int deleteByPrimaryKey(Long id);

    int deleteByLogLevel(String logLevel);
}
