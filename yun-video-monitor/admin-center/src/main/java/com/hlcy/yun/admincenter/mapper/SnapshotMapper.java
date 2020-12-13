package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.Snapshot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SnapshotMapper {

    Snapshot selectByPrimaryKey(Long id);

    int insert(Snapshot record);

    int updateByPrimaryKeySelective(Snapshot record);

    int deleteByPrimaryKey(Long id);
}
