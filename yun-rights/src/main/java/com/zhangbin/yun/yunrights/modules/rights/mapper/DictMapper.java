package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.common.mybatis.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictDO;
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
