package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.MenuVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuService {

    /**
     * 根据ID查询
     *
     * @param id
     * @return {@link MenuDO}
     */
    MenuDO queryById(long id);

    /**
     * 不分页查询满足条件的数据
     * 查询方式：
     * 1、根据 pid 查询满足条件的子菜单（直接子菜单）
     * 2、将 pid 设置为 null可以查询所有满足条件的菜单
     *
     * @param criteria 条件
     * @return /
     */
    List<MenuDO> queryAllByCriteriaWithNoPage(MenuQueryCriteria criteria);

    /**
     * 懒根据PID查询
     *
     * @param pid
     * @return {@link List<MenuDO>}
     */
    List<MenuDO> queryByPid(Long pid);

    /**
     * 懒根据组ID查询
     *
     * @param groupId
     * @param isTree
     * @return {@link List<MenuDO>}
     */
    List<MenuDO> queryByGroupId(Long groupId, Boolean isTree);

    /**
     * 获取多个菜单作为叶子节点的菜单树
     *
     * @param menuIds 作为菜单树叶子节点
     * @return {@link List<MenuDO>}
     */
    List<MenuDO> queryAncestorAndSibling(List<Long> menuIds);

    /**
     * 根据用户获取菜单
     *
     * @param userId
     * @param isTree 是否以树状返回
     * @return /
     */
    List<MenuDO> queryByUser(Long userId, Boolean isTree);

    /**
     * 根据用户获取菜单路由
     *
     * @param userId
     * @return /
     */
    List<MenuVO> getRouterMenusForUser(Long userId);

    /**
     * 创建菜单
     *
     * @param menu
     */
    void create(MenuDO menu);

    /**
     * 编辑菜单
     *
     * @param menu
     */
    void update(MenuDO menu);

    /**
     * 批量删除菜单
     *
     * @param menuIds
     */
    void deleteByMenuIds(Set<Long> menuIds);

    /**
     * 构建菜单树
     *
     * @param menus
     * @return {@link  List<MenuDO>}
     */
    List<MenuDO> buildMenuTree(Collection<MenuDO> menus);

    /**
     * 导出菜单
     *
     * @param menus    待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<MenuDO> menus, HttpServletResponse response) throws IOException;
}
