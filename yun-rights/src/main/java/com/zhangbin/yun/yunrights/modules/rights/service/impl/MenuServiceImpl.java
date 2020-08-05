//package com.zhangbin.yun.yunrights.modules.rights.service.impl;
//
//import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
//import com.zhangbin.yun.yunrights.modules.common.utils.RedisUtils;
//import com.zhangbin.yun.yunrights.modules.common.utils.ValidationUtil;
//import com.zhangbin.yun.yunrights.modules.rights.mapper.MenuDoMapper;
//import com.zhangbin.yun.yunrights.modules.rights.mapper.UserDoMapper;
//import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
//import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDo;
//import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDo;
//import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;
//import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@CacheConfig(cacheNames = "menu")
//public class MenuServiceImpl implements MenuService {
//
//    private final UserDoMapper userDoMapper;
//    private final MenuDoMapper menuDoMapper;
//    private final RoleService roleService;
//    private final RedisUtils redisUtils;
//
//    @Override
//    public List<MenuDo> queryAll(MenuQueryCriteria criteria, Boolean isQuery) throws Exception {
////        Sort sort = new Sort(Sort.Direction.ASC, "menuSort");
////        if(isQuery){
////            criteria.setPidIsNull(true);
////            List<Field> fields = QueryHelp.getAllFields(criteria.getClass(), new ArrayList<>());
////            for (Field field : fields) {
////                //设置对象的访问权限，保证对private的属性的访问
////                field.setAccessible(true);
////                Object val = field.get(criteria);
////                if("pidIsNull".equals(field.getName())){
////                    continue;
////                }
////                if (ObjectUtil.isNotNull(val)) {
////                    criteria.setPidIsNull(null);
////                    break;
////                }
////            }
////        }
////        return menuMapper.toDto(menuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),sort));
//        return null;
//    }
//
//    @Override
//    @Cacheable(key = "'id:' + #p0")
//    public MenuDo findById(long id) {
//        MenuDo menu = menuDoMapper.selectByPrimaryKey(id);
//        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
//        return menu;
//    }
//
//    /**
//     * 用户角色改变时需清理缓存
//     *
//     * @param currentUserId /
//     * @return /
//     */
//    @Override
//    @Cacheable(key = "'user:' + #p0")
//    public List<MenuDo> findByUser(Long currentUserId) {
////        List<Role> roles = roleService.findByUsersId(currentUserId);
////        Set<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
////        LinkedHashSet<Menu> menus = menuMapper.findByRoleIdsAndTypeNot(roleIds, 2);
////        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
//        return null;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void create(MenuDo resources) {
////        if (menuRepository.findByTitle(resources.getTitle()) != null) {
////            throw new EntityExistException(Menu.class, "title", resources.getTitle());
////        }
////        if (StringUtils.isNotBlank(resources.getComponentName())) {
////            if (menuRepository.findByComponentName(resources.getComponentName()) != null) {
////                throw new EntityExistException(Menu.class, "componentName", resources.getComponentName());
////            }
////        }
////        if (resources.getPid().equals(0L)) {
////            resources.setPid(null);
////        }
////        if (resources.getIFrame()) {
////            String http = "http://", https = "https://";
////            if (!(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https))) {
////                throw new BadRequestException("外链必须以http://或者https://开头");
////            }
////        }
////        menuRepository.save(resources);
////        // 计算子节点数目
////        resources.setSubCount(0);
////        // 更新父节点菜单数目
////        updateSubCnt(resources.getPid());
////        redisUtils.del("menu::pid:" + (resources.getPid() == null ? 0 : resources.getPid()));
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void update(MenuDo resources) {
////        if(resources.getId().equals(resources.getPid())) {
////            throw new BadRequestException("上级不能为自己");
////        }
////        Menu menu = menuRepository.findById(resources.getId()).orElseGet(Menu::new);
////        ValidationUtil.isNull(menu.getId(),"Permission","id",resources.getId());
////
////        if(resources.getIFrame()){
////            String http = "http://", https = "https://";
////            if (!(resources.getPath().toLowerCase().startsWith(http)||resources.getPath().toLowerCase().startsWith(https))) {
////                throw new BadRequestException("外链必须以http://或者https://开头");
////            }
////        }
////        Menu menu1 = menuRepository.findByTitle(resources.getTitle());
////
////        if(menu1 != null && !menu1.getId().equals(menu.getId())){
////            throw new EntityExistException(Menu.class,"title",resources.getTitle());
////        }
////
////        if(resources.getPid().equals(0L)){
////            resources.setPid(null);
////        }
////
////        // 记录的父节点ID
////        Long oldPid = menu.getPid();
////        Long newPid = resources.getPid();
////
////        if(StringUtils.isNotBlank(resources.getComponentName())){
////            menu1 = menuRepository.findByComponentName(resources.getComponentName());
////            if(menu1 != null && !menu1.getId().equals(menu.getId())){
////                throw new EntityExistException(Menu.class,"componentName",resources.getComponentName());
////            }
////        }
////        menu.setTitle(resources.getTitle());
////        menu.setComponent(resources.getComponent());
////        menu.setPath(resources.getPath());
////        menu.setIcon(resources.getIcon());
////        menu.setIFrame(resources.getIFrame());
////        menu.setPid(resources.getPid());
////        menu.setMenuSort(resources.getMenuSort());
////        menu.setCache(resources.getCache());
////        menu.setHidden(resources.getHidden());
////        menu.setComponentName(resources.getComponentName());
////        menu.setPermission(resources.getPermission());
////        menu.setType(resources.getType());
////        menuRepository.save(menu);
////        // 计算父级菜单节点数目
////        updateSubCnt(oldPid);
////        updateSubCnt(newPid);
////        // 清理缓存
////        delCaches(resources.getId(), oldPid, newPid);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void delete(Set<Long> menuIds) {
////        for (Menu menu : menuSet) {
////            // 清理缓存
////            delCaches(menu.getId(), menu.getPid(), null);
////            roleService.untiedMenu(menu.getId());
////            menuMapper.deleteById(menu.getId());
////            updateSubCnt(menu.getPid());
////        }
//    }
//
//
//    @Override
//    @Cacheable(key = "'pid:' + #p0")
//    public List<MenuDo> getMenus(Long pid) {
////        List<Menu> menus;
////        if (pid != null && !pid.equals(0L)) {
////            menus = menuRepository.findByPid(pid);
////        } else {
////            menus = menuRepository.findByPidIsNull();
////        }
////        return menuMapper.toDto(menus);
//        return null;
//    }
//
//    @Override
//    public List<MenuDo> queryFatherAndSiblingForMultiMenus(List<Long> menuIds) {
////        List<Menu> menuList;
////        Set<Menu> menuSet = new LinkedHashSet<>();
////        if (!CollectionUtils.isEmpty(ids)) {
////            for (Long id : ids) {
////                Menu menu = menuMapper.selectByPrimaryKey(id);
////                menuSet.addAll(menuService.getSuperior(menu));
////            }
////            menuList = menuService.buildTree(new ArrayList<>(menuSet));
////        } else {
////            menuList = menuService.getMenus(null);
////        }
////        if (menuDto.getPid() == null) {
////            menus.addAll(menuRepository.findByPidIsNull());
////            return menuMapper.toDto(menus);
////        }
////        menus.addAll(menuRepository.findByPid(menuDto.getPid()));
////        return queryFatherAndSiblingForMultiMenus(findById(menuDto.getPid()), menus);
//        return null;
//    }
//
//    @Override
//    public List<MenuDo> buildTree(List<MenuDo> menus) {
//        return null;
//    }
//
//
//    @Override
//    public void download(List<MenuDo> menuList, HttpServletResponse response) throws IOException {
//        FileUtil.downloadExcel(menuList.stream().map(e -> {
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("菜单标题", e.getMenuTitle());
//            Integer menuType = e.getMenuType();
//            map.put("菜单类型", menuType == null ? "目录" : menuType == 1 ? "菜单" : "按钮");
//            map.put("权限标识", e.getPermission());
//            map.put("创建日期", e.getCreateTime());
//            return map;
//        }).collect(Collectors.toList()), response);
//    }
//
//    /**
//     * 清理缓存
//     *
//     * @param menuId     菜单ID
//     * @param oldPid 旧的菜单父级ID
//     * @param newPid 新的菜单父级ID
//     */
//    public void delCaches(Long menuId, Long oldPid, Long newPid) {
////        List<User> users = userMapper.findByMenuId(menuId);
////        redisUtils.del("menu::menuId:" + menuId);
////        redisUtils.delByKeys("menu::user:", users.stream().map(User::getId).collect(Collectors.toSet()));
////        redisUtils.del("menu::pid:" + (oldPid == null ? 0 : oldPid));
////        redisUtils.del("menu::pid:" + (newPid == null ? 0 : newPid));
//        // 清除 Role 缓存
//        List<RoleDo> roles = roleService.findInMenuId(new ArrayList<Long>() {{
//            add(menuId);
//            add(newPid == null ? 0 : newPid);
//        }});
//        redisUtils.delByKeys("role::menuId:", roles.stream().map(RoleDo::getId).collect(Collectors.toSet()));
//    }
//}
