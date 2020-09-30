package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.datarights.NotPermission;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

@Mapper
public interface GroupMapper extends PageMapper<GroupDO>{

    GroupDO selectByPrimaryKey(Long id);

    @Select("select g_group_code from t_sys_group where g_id = #{id} for update")
    String selectGroupCodeByIdForUpdate(Long id);

    /**
     * 检查 group code 对应记录条数，用于检测 group code 唯一性
     * @param groupCode group code
     * @return 条数
     */
    @Select("select count(*) from t_sys_group where g_group_code = #{groupCode}")
    Integer countByGroupCode(String groupCode);

    Set<GroupDO> selectByPrimaryKeys(Set<Long> ids);

    /**
     * 根据用户 Id 查询用户所属组，但部门类型的组除外
     * @param userId /
     * @return {@link Set<GroupDO>}
     */
    Set<GroupDO> selectGroupByUserId(Long userId);
    /**
     * 根据用户 Id 查询用户所属组（含部门）
     * @param userId /
     * @return {@link Set<GroupDO>}
     */
    Set<GroupDO> selectByUserId(Long userId);

    Set<GroupDO> selectByUserIdForUserMapper(Long userId);

    /**
     * 根据用户名查询用户所属组 groupCode
     * @param username /
     * @return {@link Set<String>}
     */
    @NotPermission
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
