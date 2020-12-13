package com.hlcy.yun.sys.modules.rights.service;

import com.hlcy.yun.common.utils.download.DownLoadSupport;
import com.hlcy.yun.sys.modules.rights.model.$do.MenuDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.MenuQueryCriteria;
import com.hlcy.yun.sys.modules.rights.model.vo.ButtonVO;
import com.hlcy.yun.sys.modules.rights.model.vo.MenuVO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuService extends DownLoadSupport<MenuDO> {

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
     * 获取用户的按钮菜单
     *
     * @param userId
     * @return Map<String, Button> map.key is menuCode
     */
    Map<String, ButtonVO> getButtonMenusByUser(Long userId);

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
}
