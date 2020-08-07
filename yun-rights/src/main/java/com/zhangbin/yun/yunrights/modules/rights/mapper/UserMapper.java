package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends PageMapper<UserDO> {

    UserDO selectByPrimaryKey(Long id);

    UserDO selectByUserName(String userName);

    Set<UserDO> selectByMenuIs(Set<Long> menuIds);

    Set<UserDO> selectByRoleId(Long roleId);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    int deleteByPrimaryKey(Long id);

    int batchDeleteByIds(@Param("userIds") Set<Long> userIds);

    int updateByPrimaryKeySelective(UserDO record);

}
