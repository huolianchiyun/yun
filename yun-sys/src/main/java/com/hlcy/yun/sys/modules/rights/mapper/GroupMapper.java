package com.hlcy.yun.sys.modules.rights.mapper;

import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.common.mybatis.page.PageMapper;
import com.hlcy.yun.sys.modules.rights.datarights.NotPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

@Mapper
public interface GroupMapper extends PageMapper<GroupDO>{

    GroupDO selectByPrimaryKey(Long id);

    @Select("select g_group_code from t_sys_group where g_id = #{id} for update")
    String selectGroupCodeByIdForUpdate(Long id);

    Set<GroupDO> selectByPrimaryKeys(Set<Long> ids);


    Set<GroupDO> selectByUserId(Long userId);

    /**
     * 根据用户名查询用户所属组 groupCode
     * @param username /
     * @return {@link Set<String>}
     */
    @NotPermission
    Set<String> selectGroupCodesByUsername(String username);

    Set<GroupDO> selectByPid(Long Pid);

    Set<GroupDO> selectByMenuIds(@Param("menuIds") Set<Long> menuIds);

    Set<GroupDO> selectByGroupType(String groupType);

    /**
     * 最多查询 1000 条记录
     */
    Set<GroupDO> selectAll();

    int insert(GroupDO group);

    int updateByPrimaryKeySelective(GroupDO record);

    @Update("update t_sys_group set g_group_code = #{groupCode} where g_id = #{groupId}")
    void updateGroupCodeById(String groupCode, Long groupId);

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("groupIds") Set<Long> groupIds);

    int countAssociatedUser(@Param("groupIds") Set<Long> groupIds);
}
