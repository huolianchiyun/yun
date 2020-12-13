package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {

    Tag selectByPrimaryKey(Integer id);

    int insert(Tag record);

    int updateByPrimaryKeySelective(Tag record);

    int deleteByPrimaryKey(Integer id);
}
