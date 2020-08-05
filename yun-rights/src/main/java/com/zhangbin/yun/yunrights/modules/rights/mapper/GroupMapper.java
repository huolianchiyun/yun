package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMapper {

    GroupDO selectByPrimaryKey(Long id);

    GroupDO selectDeptByUserId(Long userId);

    int insert(GroupDO record);

    int insertSelective(GroupDO record);

    int updateByPrimaryKeySelective(GroupDO record);

    int deleteByPrimaryKey(Long id);
}
