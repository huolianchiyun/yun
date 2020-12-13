package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.WindowLayout;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WindowLayoutMapper {

    WindowLayout selectByPrimaryKey(Long id);

    int insert(WindowLayout record);

    int updateByPrimaryKeySelective(WindowLayout record);

    int deleteByPrimaryKey(Long id);
}
