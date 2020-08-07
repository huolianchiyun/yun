package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuService {

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
     * 根据ID查询
     *
     * @param id
     * @return {@link MenuDO}
     */
    MenuDO queryById(long id);

    /**
     * 懒根据PID查询
     *
     * @param pid
     * @return {@link List<MenuDO>}
     */
    List<MenuDO> querySubmenusByPid(Long pid);

    /**
     * 获取多个菜单作为叶子节点的菜单树
     *
     * @param menuIds 作为菜单树叶子节点
     * @return {@link List<MenuDO>}
     */
    List<MenuDO> queryAncestorAndSiblingOfMenus(List<Long> menuIds);

    /**
     * 根据用户获取菜单
     *
     * @param userId
     * @return /
     */
    List<MenuDO> queryByUser(Long userId);

    /**
     * 创建菜单
     *
     * @param menu
     */
    void createMenu(MenuDO menu);

    /**
     * 编辑菜单
     *
     * @param menu
     */
    void updateMenu(MenuDO menu);

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
