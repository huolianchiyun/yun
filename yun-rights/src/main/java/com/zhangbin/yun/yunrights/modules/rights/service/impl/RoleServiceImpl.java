package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.RoleMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.RoleMenuMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleMenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.RoleQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import com.zhangbin.yun.yunrights.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserCacheClean userCacheClean;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public RoleDO queryById(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<RoleDO> batchQueryByIds(Set<Long> ids) {
        return Optional.of(roleMapper.selectByPrimaryKeys(ids)).orElseGet(HashSet::new).stream().collect(Collectors.toList());
    }

    @Override
    public List<RoleDO> queryByUserId(Long userId) {
        return Optional.of(roleMapper.selectByUserId(userId)).orElseGet(HashSet::new).stream().collect(Collectors.toList());
    }

    @Override
    public Set<RoleDO> selectByMenuIds(Set<Long> menuIds) {
        return roleMapper.selectByMenuIds(menuIds);
    }

    @Override
    public List<RoleDO> queryAllByCriteriaWithNoPage(RoleQueryCriteria criteria) {
        return CollectionUtil.list(false, roleMapper.selectAllByCriteria(criteria));
    }

    @Override
    public PageInfo<List<RoleDO>> queryAllByCriteria(RoleQueryCriteria criteria) {
        Page<RoleDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, roleMapper);
        PageInfo<List<RoleDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<RoleDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public void createRole(RoleDO role) {
        role.setId(null); // 若创建角色时入参role.id不为null，默认将其设置为null
        checkRoleLevel(role.getLevel());
        Assert.notNull(roleMapper.selectByRoleName(role.getRoleName()), "角色名已存在，请重新命名！");
        roleMapper.insert(role);
        updateAssociatedMenu(role);
    }

    @Override
    public void updateRole(RoleDO role) {
        checkRoleLevel(role.getLevel());
        roleMapper.updateByPrimaryKeySelective(role);
        updateAssociatedMenu(role);
        // 更新相关缓存
        clearCaches(role.getId(), null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRoles(Set<Long> roleIds) {
        for (Long id : roleIds) {
            RoleDO role = queryById(id);
            checkRoleLevel(role.getLevel());
        }
        // 验证是否被用户或组关联
        Assert.isTrue(isAssociatedUserOrGroup(roleIds), "将要删除的角色存在用户或组关联，请解除关联关系后，再尝试！");
        roleMapper.deleteByIds(roleIds);
        roleMenuMapper.deleteByRoleIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateAssociatedMenu(RoleDO role) {
        RoleDO roleDb = queryById(role.getId());
        checkRoleLevel(roleDb.getLevel());
        Set<RoleMenuDO> roleMenus = Optional.of(role.getMenus()).orElseGet(HashSet::new)
                .stream().map(e -> new RoleMenuDO(role.getId(), e.getId())).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(roleMenus)) {
            roleMenuMapper.deleteByRoleIds(CollectionUtil.newHashSet(role.getId()));
            roleMenuMapper.batchInsert(roleMenus);
        }
        List<UserDO> users = Optional.of(userMapper.selectByRoleId(roleDb.getId()))
                .orElseGet(HashSet::new).stream().collect(Collectors.toList());
        clearCaches(role.getId(), users);
    }

    @Override
    public void download(List<RoleDO> roleList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.of(roleList).orElseGet(ArrayList::new).stream().map(RoleDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public Boolean isAssociatedUserOrGroup(Set<Long> roleIds) {
        return roleMapper.countAssociatedUserAndRole(roleIds) > 0;
    }

    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> getGrantedAuthorities(UserDO user) {
        Set<String> permissions = new HashSet<>(1);
        if (user.isAdmin()) {  // 如果是管理员直接返回
            permissions.add("admin");
        } else {
            List<RoleDO> roles = Optional.of(roleMapper.selectByUserId(user.getId()))
                    .orElseGet(HashSet::new).stream().collect(Collectors.toList());
            permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                    .map(MenuDO::getPermission).collect(Collectors.toSet());
        }
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getLevelOfCurrentUserMaxRole() {
        return Collections.min(queryByUserId(SecurityUtils.getCurrentUserId())
                .stream()
                .map(RoleDO::getLevel)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean hasSupperLevelInUsers(Integer levelOfCurrentUserMaxRole, Set<Long> userIds) {
        return roleMapper.countSuperLevelInUserIds(levelOfCurrentUserMaxRole, userIds) > 0;
    }

    /**
     * 清理缓存
     *
     * @param roleId
     * @param users
     */
    private void clearCaches(Long roleId, List<UserDO> users) {
        users = CollectionUtil.isEmpty(users) ? CollectionUtil.list(false, userMapper.selectByRoleId(roleId)) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheClean.cleanUserCache(item.getUserName()));
            Set<Long> userIds = users.stream().map(UserDO::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATE_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtils.del(CacheKey.ROLE_ID + roleId);
        }
    }
}
