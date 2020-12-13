package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.Alarm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper {

    Alarm selectByPrimaryKey(Long id);

    int insert(Alarm record);

    int updateByPrimaryKeySelective(Alarm record);

    int deleteByPrimaryKey(Long id);

}
