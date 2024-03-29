package com.hlcy.yun.sys.modules.rights.mapper;

import com.hlcy.yun.common.mybatis.page.PageMapper;
import com.hlcy.yun.sys.modules.rights.datarights.NotPermission;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends PageMapper<UserDO> {
    @NotPermission
    UserDO selectByPrimaryKey(Long id);

    @NotPermission
    @Select("select u_username from t_sys_user where u_id = #{id}")
    String selectUsernameById(Long id);

    @NotPermission
    UserDO selectByUsername(String username);

    @NotPermission
    Set<UserDO> selectByIds(@Param("ids") Set<Long> ids);

    @NotPermission
    Set<UserDO> selectByGroupId(Long groupId);

    @NotPermission
    Set<UserDO> selectByDeptId(Long deptId);

    @NotPermission
    Set<UserDO> selectByGroupIds(@Param("groupIds") Set<Long> groupIds);

    int insert(UserDO record);

    int batchInsert(Set<UserDO> list);

    int updateByPrimaryKeySelective(UserDO record);

    int resetPwd(UserDO record);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("ids") Set<Long> ids);

}
