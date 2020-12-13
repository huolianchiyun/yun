package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.Device;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper {

    int insert(Device record);

}
