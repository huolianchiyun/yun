package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface GroupMapper extends PageMapper<GroupDO> {

    GroupDO selectByPrimaryKey(Long id);

    GroupDO selectByUserId(Long userId);

    Set<GroupDO> selectByPid(Long Pid);

    Set<GroupDO> selectByRoleId(Long roleId);

    int insert(GroupDO record);

    int updateByPrimaryKeySelective(GroupDO record);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("groupIds") Set<Long> groupIds);

    int countAssociatedUser(@Param("groupIds") Set<Long> groupIds);
}
