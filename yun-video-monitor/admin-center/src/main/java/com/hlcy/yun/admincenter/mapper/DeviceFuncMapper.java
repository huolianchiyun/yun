package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.DeviceFunc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceFuncMapper {

    int insert(DeviceFunc record);

    DeviceFunc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceFunc record);

    int deleteByPrimaryKey(Integer id);
}
