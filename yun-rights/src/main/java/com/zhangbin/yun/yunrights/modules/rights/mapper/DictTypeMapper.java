package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.common.mybatis.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface DictTypeMapper extends PageMapper<DictTypeDO> {
    DictTypeDO selectByPrimaryKey(Long id);

    Set<DictTypeDO> selectAll();

    int selectUsedCount(Long id);

    int insert(DictTypeDO record);

    int updateByPrimaryKeySelective(DictTypeDO record);

    int deleteByIds(@Param("ids") Set<Long> ids);
}
