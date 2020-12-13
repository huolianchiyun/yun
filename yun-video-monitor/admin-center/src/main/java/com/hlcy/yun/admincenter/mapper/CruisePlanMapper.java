package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.CruisePlan;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CruisePlanMapper {

    CruisePlan selectByPrimaryKey(Long id);

    int insert(CruisePlan record);

    int updateByPrimaryKeySelective(CruisePlan record);

    int deleteByPrimaryKey(Long id);


}
