package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictionaryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryMapper  extends PageMapper<DictionaryDO> {
    DictionaryDO selectByPrimaryKey(Long id);

    int insert(DictionaryDO record);

    int updateByPrimaryKeySelective(DictionaryDO record);

    int deleteByPrimaryKey(Long id);
}
