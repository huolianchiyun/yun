package com.zhangbin.yun.yunrights.modules.rights.service;


import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface MenuService {

    /**
     * 查询全部数据
     * @param queryConditions 条件
     * @param isQuery /
     * @throws Exception /
     * @return /
     */
    List<MenuDO> queryAll(MenuQueryCriteria queryConditions, Boolean isQuery) throws Exception;

    /**
     * 根据ID查询
     * @param id
     * @return {@link MenuDO}
     */
    MenuDO findById(long id);

    /**
     * 创建
     * @param resource
     */
    void create(MenuDO resource);

    /**
     * 编辑
     * @param resource
     */
    void update(MenuDO resource);

    /**
     * 删除
     * @param menuIds
     */
    void delete(Set<Long> menuIds);

    /**
     * 构建菜单树
     * @param menus
     * @return {@link  List<  MenuDO  >}
     */
    List<MenuDO> buildTree(List<MenuDO> menus);



    /**
     * 导出
     * @param menuList 待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<MenuDO> menuList, HttpServletResponse response) throws IOException;

    /**
     * 懒加载菜单数据
     * @param pid
     * @return {@link List<  MenuDO  >}
     */
    List<MenuDO> getMenus(Long pid);

    /**
     * 根据多个菜单ID，为其获取同级与上级数据
     * @param menuIds
     * @return {@link List<  MenuDO  >}
     */
    List<MenuDO> queryFatherAndSiblingForMultiMenus(List<Long> menuIds);

    /**
     * 根据当前用户获取菜单
     * @param currentUserId
     * @return /
     */
    List<MenuDO> findByUser(Long currentUserId);
}
