package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.HistoryVideo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryVideoMapper {

    HistoryVideo selectByPrimaryKey(Long id);

    int insert(HistoryVideo record);

    int updateByPrimaryKeySelective(HistoryVideo record);

    int deleteByPrimaryKey(Long id);
}
