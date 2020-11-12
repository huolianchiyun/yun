package com.yun.sys.modules.rights.mapper;

import com.yun.sys.modules.rights.model.$do.DictDO;
import com.yun.common.mybatis.page.PageMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface DictMapper extends PageMapper<DictDO> {
    DictDO selectByPrimaryKey(Long id);

    Set<DictDO> selectByTypeCode(String typeCode);

    Boolean selectStatusById(Long id);

    int insert(DictDO record);

    int updateByPrimaryKeySelective(DictDO record);

    int deleteByIds(@Param("ids") Set<Long> ids);
}
