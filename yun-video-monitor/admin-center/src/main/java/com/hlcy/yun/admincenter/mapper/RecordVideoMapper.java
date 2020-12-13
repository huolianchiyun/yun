package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.RecordVideo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordVideoMapper {

    RecordVideo selectByPrimaryKey(Long id);

    int insert(RecordVideo record);

    int updateByPrimaryKeySelective(RecordVideo record);

    int deleteByPrimaryKey(Long id);
}
