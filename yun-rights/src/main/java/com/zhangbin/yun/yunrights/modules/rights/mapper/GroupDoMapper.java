package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupDoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GroupDo record);

    int insertSelective(GroupDo record);

    GroupDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupDo record);

    int updateByPrimaryKey(GroupDo record);
}