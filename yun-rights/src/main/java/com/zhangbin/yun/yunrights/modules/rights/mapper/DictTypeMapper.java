package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictTypeDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.Set;

@Mapper
public interface DictTypeMapper {
    Set<DictTypeDO> selectByPrimaryKey();

    Set<DictTypeDO> selectAll();

    int insert(DictTypeDO record);

    int updateByPrimaryKeySelective(DictTypeDO record);

    int deleteByPrimaryKey(Long id);
}
