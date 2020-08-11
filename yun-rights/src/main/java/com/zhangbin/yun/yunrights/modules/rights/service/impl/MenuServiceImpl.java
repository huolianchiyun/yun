package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import static com.zhangbin.yun.yunrights.modules.common.constant.Constants.HTTP;
import static com.zhangbin.yun.yunrights.modules.common.constant.Constants.HTTPS;
import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.MenuMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.RoleMenuMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "menu")
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public List<MenuDO> queryAllByCriteriaWithNoPage(MenuQueryCriteria criteria) {
        return SetUtils.toListWithSorted(menuMapper.selectAllByCriteria(criteria), MenuDO::compareTo);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public MenuDO queryById(long id) {
        MenuDO menu = menuMapper.selectByPrimaryKey(id);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return menu;
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<MenuDO> querySubmenusByPid(Long pid) {
        return SetUtils.toListWithSorted(menuMapper.selectByPid(pid), MenuDO::compareTo);
    }

    @Override
    public List<MenuDO> queryAncestorAndSiblingOfMenus(List<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return querySubmenusByPid(null);
        }
        // 获取所有菜单
        Set<MenuDO> allMenus = menuMapper.selectAllByCriteria(null);
        // 删掉 menuIds 的直接子菜单，使其连接断路
        List<MenuDO> menus = allMenus.stream().filter(e -> !menuIds.contains(e.getPid())).collect(Collectors.toList());
        return buildMenuTree(menus);
    }

    @Override
    @Cacheable(key = "'user:' + #p0")
    public List<MenuDO> queryByUser(Long userId) {
        List<RoleDO> roles = roleService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        Set<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toSet());
        Set<MenuDO> menuSet = menuMapper.selectByRoleIds(roleIds);
        return buildMenuTree(menuSet);
    }

    @Override
    public void createMenu(MenuDO menu) {
        menu.setId(null);  // 若创建菜单时入参menu.id不为null，默认将其设置为null
        validateExternalLink(menu);
        menuMapper.insert(menu);
        if(Objects.nonNull(redisUtils)){
            redisUtils.del("menu::pid:" + (menu.getPid() == null ? 0 : menu.getPid()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuDO updatingMenu) {
        Assert.isTrue(updatingMenu.getId().equals(updatingMenu.getPid()), "上级不能为自己!");
        MenuDO menuDb = menuMapper.selectByPrimaryKey(updatingMenu.getId());
        Assert.isNull(menuDb, "修改的菜单不存在！");
        validateExternalLink(updatingMenu);
        updatingMenu.setOldPid(menuDb.getPid());
        menuMapper.updateByPrimaryKeySelective(updatingMenu);
        // 清理缓存
        clearCaches(CollectionUtil.newHashSet(updatingMenu));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuIds(Set<Long> menuIds) {
        // 获取要删除菜单及子孙菜单
        Set<MenuDO> posterityMenuWithSelf = new HashSet<>(getPosterityMenusWithSelf(menuIds));
        Set<Long> deletingMenuIds = posterityMenuWithSelf.stream().map(MenuDO::getId).collect(Collectors.toSet());
        menuMapper.batchDeleteByIds(deletingMenuIds);
        // 将要删除的菜单与角色解绑
        roleMenuMapper.deleteByMenuIds(deletingMenuIds);
        // 清理缓存
        clearCaches(posterityMenuWithSelf);
    }

    @Override
    public void download(List<MenuDO> menus, HttpServletResponse response) throws IOException {
        List<MenuDO> menuSorted = new ArrayList<>();
        buildMenuTree(menus).forEach(new CollectChildren<>(menuSorted));
        FileUtil.downloadExcel(menuSorted.stream().map(MenuDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public List<MenuDO> buildMenuTree(Collection<MenuDO> menus) {
        return new TreeBuilder<MenuDO>().buildTree(menus);
    }

    /**
     * 获取菜单集合的子孙菜单及自己
     *
     * @param menuIds 菜单集合
     */
    private List<MenuDO> getPosterityMenusWithSelf(Set<Long> menuIds) {
        Set<MenuDO> allMenus = menuMapper.selectAllByCriteria(null);
        List<MenuDO> tree = new TreeBuilder<MenuDO>().buildTree(allMenus, menuIds);
        List<MenuDO> menusSorted = new ArrayList<>();
        tree.forEach(new CollectChildren<>(menusSorted));
        return menusSorted;
    }

    private void validateExternalLink(MenuDO menu) {
        if (menu.getExternalLink()) {
            String url = menu.getAccessUrl().toLowerCase();
            Assert.isTrue(!(url.startsWith(HTTP) || url.startsWith(HTTPS)),
                    "外链必须以http://或者https://开头");
        }
    }

    /**
     * 清理菜单相关缓存
     *
     * @param menuSet 不能为 empty或 null
     */
    private void clearCaches(@NotEmpty Set<MenuDO> menuSet) {
        if(Objects.nonNull(redisUtils)){
            Set<Long> menuIds = menuSet.stream().map(MenuDO::getId).collect(Collectors.toSet());
            List<UserDO> users = CollectionUtil.list(false, userMapper.selectByMenuIs(menuIds));
            redisUtils.delByKeys("menu::user:", users.stream().map(UserDO::getId).collect(Collectors.toSet()));

            menuSet.forEach(menu -> {
                redisUtils.del("menu::menuId:" + menu.getId());
                redisUtils.del("menu::pid:" + (menu.getPid()));
                redisUtils.del("menu::pid:" + (menu.getOldPid()));
                menuIds.add(menu.getPid());  // 后续清除角色缓存时， 也要清除其父菜单对应的角色缓存
            });

            // 清除 Role 缓存
            Set<RoleDO> roles = roleService.selectByMenuIds(menuIds);
            redisUtils.delByKeys("role::menuId:", roles.stream().map(RoleDO::getId).collect(Collectors.toSet()));
        }
    }
}
