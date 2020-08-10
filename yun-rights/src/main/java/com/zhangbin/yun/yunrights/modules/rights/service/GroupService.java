package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GroupService {
    /**
     * 不分页查询满足条件的数据
     * 查询方式：
     * 1、根据 pid 查询满足条件的子组（直接子组）
     * 2、将 pid 设置为 null可以查询所有满足条件的组
     *
     * @param criteria 条件
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> queryAllByCriteriaWithNoPage(GroupQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id 部门 ID
     * @return
     */
    GroupDO queryById(Long id);

    /**
     * 根据PID查询子部门
     *
     * @param pid 父 ID
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> queryByPid(Long pid);

    /**
     * 根据部门 id 集合获取同级与上级数据
     *
     * @param groupIds 部门 ID 集合
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> queryAncestorAndSiblingOfDepts(Set<Long> groupIds);

    /**
     * 根据角色ID查询
     *
     * @param roleId 角色 ID
     * @return {@link Set<GroupDO>}
     */
    List<GroupDO> queryByRoleId(Long roleId);

    /**
     * 创建部门
     *
     * @param group 部门
     */
    void createGroup(GroupDO group);

    /**
     * 编辑部门
     *
     * @param group 部门
     */
    void updateGroup(GroupDO group);

    /**
     * 批量删除
     *
     * @param groupIds 将要删除的部门 ID 集合
     */
    void deleteByGroupIds(Set<Long> groupIds);

    /**
     * 构建部门树
     *
     * @param depts  用于构建部门树的部门集合源数据
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> buildGroupTree(Collection<GroupDO> depts);

    /**
     * 导出部门数据
     *
     * @param depts 待导出的数据
     * @param response 客户端响应
     * @throws IOException /
     */
    void download(List<GroupDO> depts, HttpServletResponse response) throws IOException;

    /**
     * 验证是否被用户关联
     *
     * @param groupIds 验证的组集合ID
     */
    Boolean isAssociatedUser(Set<Long> groupIds);
}
