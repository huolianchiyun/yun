package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.datarights.NotPermission;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupMenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
@NotPermission
public interface UserMapper extends PageMapper<UserDO> {

    UserDO selectByPrimaryKey(Long id);

    UserDO selectByUsername(String username);

    Set<UserDO> selectByIds(@Param("ids") Set<Long> ids);

    Set<UserDO> selectByMenuIs(@Param("menuIds") Set<Long> menuIds);

    Set<UserDO> selectByGroupId(Long groupId);

    Set<UserDO> selectByGroupIds(@Param("groupIds") Set<Long> groupIds);

    int insert(UserDO record);

    int updateByPrimaryKeySelective(UserDO record);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("userIds") Set<Long> userIds);

}
