package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GroupService {

    /**
     * 分页查询满足条件的组
     *
     * @return {@link PageInfo<List<GroupDO>> }
     */
    PageInfo<List<GroupDO>> queryAllByCriteria(GroupQueryCriteria criteria);

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

     * @param id 组ID
     * @return {@link GroupDO}
     */
    GroupDO queryById(Long id);

    /**
     * 根据ID集合查询
     *
     * @param ids 组ID集合
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> queryByIds(Set<Long> ids);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> queryByUserId(Long userId);

    /**
     * 根据菜单集合查询
     *
     * @param menuIds 菜单ID集合
     * @return {@link GroupDO}
     */
    Set<GroupDO> queryByMenuIds(Set<Long> menuIds);

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
    List<GroupDO> queryAncestorAndSiblingOfGroups(Set<Long> groupIds);

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
    void deleteByIds(Set<Long> groupIds);

    /**
     * 添加用户到组
     * @param userIds 用户ID集合
     * @param groupId 组ID
     */
    void addUsersIntoGroup(Set<Long> userIds, Long groupId);

    /**
     * 构建部门树
     *
     * @param depts 用于构建部门树的部门集合源数据
     * @return {@link List<GroupDO>}
     */
    List<GroupDO> buildGroupTree(Collection<GroupDO> depts);

    /**
     * 导出部门数据
     *
     * @param depts    待导出的数据
     * @param response 客户端响应
     * @throws IOException /
     */
    void download(List<GroupDO> depts, HttpServletResponse response) throws IOException;

    /**
     * 验证是否被用户关联
     *
     * @param groupIds 验证的组集合ID
     * @return 关联返回 true，反之，返回 false
     */
    Boolean isAssociatedUser(Set<Long> groupIds);

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> getGrantedAuthorities(UserDO user);
}
