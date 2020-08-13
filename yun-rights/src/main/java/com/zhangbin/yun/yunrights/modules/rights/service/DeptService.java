package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DeptQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 在本系统设计中，部门本质是组，是一种特殊的组
 */
public interface DeptService {

    /**
     * 不分页查询满足条件的数据
     * 查询方式：
     * 1、根据 pid 查询满足条件的子部门（直接子部门）
     * 2、将 pid 设置为 null可以查询所有满足条件的部门
     *
     * @param criteria 条件
     * @return {@link List<DeptDTO>}
     */
    List<DeptDTO> queryAllByCriteriaWithNoPage(DeptQueryCriteria criteria);

    /**
     * 根据部门 id 集合获取同级与上级数据
     *
     * @param deptIds 部门 ID 集合
     * @return {@link List<DeptDTO>}
     */
    List<DeptDTO> queryAncestorAndSiblingOfDepts(Set<Long> deptIds);

    /**
     * 根据ID查询
     *
     * @param id 部门 ID
     * @return
     */
    DeptDTO queryById(Long id);

    /**
     * 根据PID查询子部门
     *
     * @param pid 父 ID
     * @return {@link List<DeptDTO>}
     */
    List<DeptDTO> queryByPid(Long pid);

    /**
     * 创建部门
     *
     * @param dept 部门
     */
    void createDept(DeptDTO dept);

    /**
     * 编辑部门
     *
     * @param dept 部门
     */
    void updateDept(DeptDTO dept);

    /**
     * 批量删除
     *
     * @param deptIds 将要删除的部门 ID 集合
     */
    void deleteByDeptIds(Set<Long> deptIds);

    /**
     * 构建部门树
     *
     * @param depts  用于构建部门树的部门集合源数据
     * @return {@link List<DeptDTO>}
     */
    List<DeptDTO> buildDeptTree(Collection<DeptDTO> depts);

    /**
     * 导出部门数据
     *
     * @param depts 待导出的数据
     * @param response 客户端响应
     * @throws IOException /
     */
    void download(List<DeptDTO> depts, HttpServletResponse response) throws IOException;

    /**
     * 验证是否被用户关联
     *
     * @param deptIds 验证的部门集合 ID
     */
    Boolean isAssociatedUser(Set<Long> deptIds);
}
