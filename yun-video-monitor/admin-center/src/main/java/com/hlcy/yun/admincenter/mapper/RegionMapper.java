package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.Region;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMapper {

    Region selectByPrimaryKey(Integer id);

    int insert(Region record);

    int updateByPrimaryKeySelective(Region record);

    int deleteByPrimaryKey(Integer id);
}
