package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

@Mapper
public interface GroupMapper extends PageMapper<GroupDO> {

    GroupDO selectByPrimaryKey(Long id);

    @Select("select g_group_code from t_sys_group where g_id = #{id} for update")
    GroupDO selectByIdForUpdate(Long id);

    Set<GroupDO> selectByPrimaryKeys(Set<Long> ids);

    /**
     * 根据用户Id查询用户所属组，但部门类型的组除外
     * @param userId /
     * @return {@link Set<GroupDO>}
     */
    Set<GroupDO> selectByUserId(Long userId);

    /**
     * 根据用户名查询用户所属组 groupCode
     * @param username /
     * @return {@link Set<String>}
     */
    Set<String> selectByUsername(String username);

    Set<GroupDO> selectByPid(Long Pid);

    GroupDO selectDeptByUserId(Long userId);

    Set<GroupDO> selectByMenuIds(@Param("menuIds") Set<Long> menuIds);

    int insert(GroupDO group);

    int updateByPrimaryKeySelective(GroupDO record);

    @Update("update t_sys_group set g_group_code = #{groupCode} where g_id = #{groupId}")
    void updateGroupCodeById(String groupCode, Long groupId);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("groupIds") Set<Long> groupIds);

    int countAssociatedUser(@Param("groupIds") Set<Long> groupIds);
}
