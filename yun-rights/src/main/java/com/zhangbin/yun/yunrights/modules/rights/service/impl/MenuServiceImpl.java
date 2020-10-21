package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import static com.zhangbin.yun.yunrights.modules.common.xcache.CacheKey.*;
import static com.zhangbin.yun.common.constant.Constants.HTTP;
import static com.zhangbin.yun.common.constant.Constants.HTTPS;
import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.common.spring.redis.RedisUtils;
import com.zhangbin.yun.common.spring.security.SecurityUtils;
import com.zhangbin.yun.common.utils.io.FileUtil;
import com.zhangbin.yun.common.utils.collect.SetUtils;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.MenuApiRightsMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.MenuMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.GroupMenuMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuApiRightsDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.MenuVO;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = MENU)
public class MenuServiceImpl implements MenuService {

    private final GroupService groupService;
    private final MenuMapper menuMapper;
    private final GroupMenuMapper groupMenuMapper;
    private final MenuApiRightsMapper menuApiRightsMapper;
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
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<MenuDO> queryByPid(Long pid) {
        return SetUtils.toListWithSorted(menuMapper.selectByPid(pid), MenuDO::compareTo);
    }

    @Override
    @Cacheable(key = "'group:' + #p0 + #p1")
    public List<MenuDO> queryByGroupId(Long groupId, Boolean isTree) {
        Set<MenuDO> menuSet = menuMapper.selectByGroupIds(CollectionUtil.set(false, groupId));
        if (CollectionUtils.isEmpty(menuSet)) {
            return new ArrayList<>();
        }
        return isTree ? buildMenuTree(menuSet) : new ArrayList<>(menuSet);
    }

    @Override
    public List<MenuDO> queryAncestorAndSibling(List<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return queryByPid(null);
        }
        // 获取所有菜单
        Set<MenuDO> allMenus = menuMapper.selectAllByCriteria(null);
        // 删掉 menuIds 的直接子菜单，使其连接断路
        List<MenuDO> menus = allMenus.stream().filter(e -> !menuIds.contains(e.getPid())).collect(Collectors.toList());
        return buildMenuTree(menus);
    }

    @Override
    @Cacheable(value = BIND_USER + MENU, key = "'" + UserIdKey + "' + #p0 +'" + ":" + "'+ #p1")
    public List<MenuDO> queryByUser(Long userId, Boolean isTree) {
        Set<MenuDO> menuSet;
        // 如果是admin，则返回所有菜单
        if (SecurityUtils.isAdmin()) {
            menuSet = menuMapper.selectAllByCriteria(null);
        } else {
            List<GroupDO> groups = groupService.queryByUserId(userId);
            if (CollectionUtils.isEmpty(groups)) {
                return new ArrayList<>();
            }
            Set<Long> groupIds = groups.stream().map(GroupDO::getId).collect(Collectors.toSet());
            menuSet = menuMapper.selectByGroupIds(groupIds);
        }
        return isTree ? buildMenuTree(menuSet) : new ArrayList<>(menuSet);
    }

    @Override
    @Cacheable(value = BIND_USER + MENU, key = "'router:" + UserIdKey + "' + #p0")
    //  + MENU 目的是避免 redis Hash的 key重名冲突
    public List<MenuVO> getRouterMenusForUser(Long userId) {
        if (SecurityUtils.isAdmin()) {
            return buildMenuForRouter(menuMapper.selectRouterMenus());
        }
        return buildMenuForRouter(menuMapper.selectRouterMenusByUserId(userId));
    }

    @Override
    @Caching(evict = {@CacheEvict(key = "'id:' + #p0.pid"), @CacheEvict(key = "'pid:' + #p0.pid")})
    public void create(MenuDO menu) {
        validate(menu, true);
        menuMapper.insert(menu);
        updateAssociatedApiRights(menu, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDO updatingMenu) {
        validate(updatingMenu, false);
        Assert.isTrue(!updatingMenu.getId().equals(updatingMenu.getPid()), "上级不能为自己!");
        MenuDO menuDb = menuMapper.selectByPrimaryKey(updatingMenu.getId());
        Assert.notNull(menuDb, "修改的菜单不存在！");
        updatingMenu.setOldPid(menuDb.getPid());
        menuMapper.updateByPrimaryKeySelective(updatingMenu);
        updateAssociatedApiRights(updatingMenu, false);
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
        groupMenuMapper.deleteByMenuIds(deletingMenuIds);
        // 将要删除的菜单与 api-rights 解绑
        menuApiRightsMapper.deleteByMenuIds(deletingMenuIds);
        // 清理缓存
        clearCaches(posterityMenuWithSelf);
    }

    @Override
    public void downloadExcel(Collection<MenuDO> collection, HttpServletResponse response) {
        List<MenuDO> menuSorted = new ArrayList<>();
        buildMenuTree(collection).forEach(new CollectChildren<>(menuSorted));
        FileUtil.downloadExcel(menuSorted.stream().map(MenuDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public List<MenuDO> buildMenuTree(Collection<MenuDO> menus) {
        return new TreeBuilder<MenuDO>().buildTree(menus);
    }

    /**
     * 为前端路由构造菜单树
     *
     * @param menus
     * @return
     */
    private List<MenuVO> buildMenuForRouter(Collection<MenuDO> menus) {
        if (CollectionUtil.isEmpty(menus)) {
            return new ArrayList<>(0);
        }
        List<MenuDO> tree = new TreeBuilder<MenuDO>().buildTree(menus);
        return tree.stream().sorted(MenuDO::compareTo).map(MenuDO::toMenuVO).collect(Collectors.toList());
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

    /**
     * 修改菜单关联的API访问权限
     *
     * @param menu    创建或修改的菜单
     * @param isCreate 是否是创建菜单
     */
    private void updateAssociatedApiRights(MenuDO menu, boolean isCreate) {
        if (CollectionUtil.isNotEmpty(menu.getApiUrls())) {
            Set<MenuApiRightsDO> menuApiRightsSet = menu.getApiUrls().stream()
                    .map(apiUrl -> new MenuApiRightsDO(menu.getId(), apiUrl))
                    .collect(Collectors.toSet());
            if (!isCreate) {
                menuApiRightsMapper.deleteByMenuIds(CollectionUtil.newHashSet(menu.getId()));
            }
            menuApiRightsMapper.batchInsert(menuApiRightsSet);
        }
    }

    private void validate(MenuDO menu, boolean isCreate) {
        validateExternalLink(menu);
        if (!isCreate) return;
        if (MenuDO.MenuType.Dir == menu.getMenuType() || MenuDO.MenuType.MENU == menu.getMenuType()) {
            Assert.isTrue(StringUtils.hasText(menu.getRouterPath()), "router path 不能为空！");
            if (MenuDO.MenuType.MENU == menu.getMenuType()) {
                Assert.isTrue(StringUtils.hasText(menu.getRouterName()), "router name 不能为空！");
                Assert.isTrue(StringUtils.hasText(menu.getComponent()), "component 不能为空！");
            }
        }
    }

    private void validateExternalLink(MenuDO menu) {
        if (menu.getExternalLink()) {
            String accessUrl = menu.getRouterPath();
            if (StringUtils.hasText(accessUrl)) {
                String url = accessUrl.toLowerCase();
                Assert.isTrue(url.startsWith(HTTP) || url.startsWith(HTTPS),
                        "外链必须以http://或者https://开头");
            }
        }
    }

    /**
     * 清理菜单相关缓存
     *
     * @param menuSet 不能为 empty或 null
     */
    private void clearCaches(Set<MenuDO> menuSet) {
        if (Objects.nonNull(redisUtils) && CollectionUtil.isNotEmpty(menuSet)) {
            Set<Long> menuIds = menuSet.stream().map(MenuDO::getId).collect(Collectors.toSet());
            menuSet.forEach(menu -> {
                redisUtils.del(MENU_ID + menu.getId(), MENU_PID + menu.getOldPid(), MENU_PID + menu.getPid());
                menuIds.add(menu.getPid());  // 后续清除角色缓存时， 也要清除其父菜单对应的角色缓存
            });
            Set<GroupDO> groups = groupService.queryByMenuIds(menuIds);
            if(CollectionUtil.isNotEmpty(groups)){
                groupService.clearCaches(groups);
            }
        }
    }
}
