package com.hlcy.yun.sys.modules.rights.service;

import com.hlcy.yun.sys.modules.rights.model.$do.DeptDO;
import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.DeptQueryCriteria;
import com.hlcy.yun.common.utils.download.DownLoadSupport;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 在本系统设计中，部门本质是组，是一种特殊的组
 */
public interface DeptService extends PageService<DeptQueryCriteria, DeptDO>, DownLoadSupport<DeptDO> {

    /**
     * 根据 ID查询
     *
     * @param id 部门 ID
     * @return
     */
    DeptDO queryById(Long id);

    /**
     * 不分页查询满足条件的数据
     * 查询方式：
     * 1、根据 pid 查询满足条件的子部门（直接子部门）
     * 2、将 pid 设置为 null可以查询所有满足条件的部门
     *
     * @param criteria 条件
     * @return {@link List<DeptDO>}
     */
    List<DeptDO> queryAllByCriteriaWithNoPage(DeptQueryCriteria criteria);

    /**
     * 根据部门 id 集合获取同级与上级数据
     *
     * @param deptIds 部门 ID 集合
     * @return {@link List<GroupDO>}
     */
    List<DeptDO> queryAncestorAndSibling(Set<Long> deptIds);

    /**
     * 根据 PID查询子部门
     *
     * @param pid 父 ID
     * @return {@link List<DeptDO>}
     */
    List<DeptDO> queryByPid(Long pid);

    /**
     * 创建部门
     *
     * @param dept 部门
     */
    void create(DeptDO dept);

    /**
     * 编辑部门
     *
     * @param dept 部门
     */
    void update(DeptDO dept);

    /**
     * 批量删除
     *
     * @param deptIds 将要删除的部门 ID 集合
     */
    void deleteByDeptIds(Set<Long> deptIds);

    /**
     * 构建部门树
     *
     * @param depts 用于构建部门树的部门集合源数据
     * @return {@link List<DeptDO>}
     */
    List<DeptDO> buildDeptTree(Collection<DeptDO> depts);

    /**
     * 验证是否被用户关联
     *
     * @param deptIds 验证的部门集合 ID
     */
    Boolean isAssociatedUser(Set<Long> deptIds);
}
