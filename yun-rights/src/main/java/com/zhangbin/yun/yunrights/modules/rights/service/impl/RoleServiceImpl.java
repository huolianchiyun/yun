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
    private final RedisUtils redisUtils;
    private final UserCacheClean userCacheClean;

    @Override
    public RoleDO queryById(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<RoleDO> batchQueryByIds(Set<Long> ids) {
        return roleMapper.selectByPrimaryKeys(ids);
    }

    @Override
    public List<RoleDO> queryByUserId(Long userId) {
        return roleMapper.selectRoleByUserId(userId);
    }

    @Override
    public List<RoleDO> queryAllByCriteriaWithNoPage(RoleQueryCriteria criteria) {
        return roleMapper.selectAllByCriteria(criteria);
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
        updateAssociatedMenuForRole(role);
    }

    @Override
    public void updateRole(RoleDO role) {
        checkRoleLevel(role.getLevel());
        roleMapper.updateByPrimaryKeySelective(role);
        updateAssociatedMenuForRole(role);
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    public void batchDeleteRoles(Set<Long> roleIds) {
        for (Long id : roleIds) {
            RoleDO role = queryById(id);
            checkRoleLevel(role.getLevel());
        }
        // 验证是否被用户关联
        Assert.isTrue(isAssociatedUsers(roleIds), "将要删除的角色存在用户关联，请解除关联关系后，再尝试！");
        roleMapper.batchDeleteByIds(roleIds);
    }

    @Override
    public void download(List<RoleDO> roleList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> mapList = roleList.stream().map(e -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", e.getRoleName());
            map.put("角色级别", e.getLevel());
            map.put("描述", e.getDescription());
            map.put("创建日期", e.getCreateTime());
            return map;
        }).collect(Collectors.toList());
        FileUtil.downloadExcel(mapList, response);
    }

    @Override
    public Boolean isAssociatedUsers(Set<Long> roleIds) {
        return roleMapper.countAssociatedUsers(roleIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateAssociatedMenuForRole(RoleDO role) {
        RoleDO roleDb = queryById(role.getId());
        checkRoleLevel(roleDb.getLevel());
//        roleMapper.updateByPrimaryKeySelective(roleDb);
        Set<RoleMenuDO> roleMenus = Optional.of(role.getMenus()).orElseGet(HashSet::new)
                .stream().map(e -> new RoleMenuDO(role.getId(), e.getId())).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(roleMenus)) {
            roleMenuMapper.batchDeleteByRoleId(role.getId());
            roleMenuMapper.batchInsert(roleMenus);
        }
        List<UserDO> users = userMapper.selectByRoleId(roleDb.getId());
        delCaches(role.getId(), users);
    }

    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> getGrantedAuthorities(UserDO user) {
        Set<String> permissions = new HashSet<>(1);
        if (user.isAdmin()) {  // 如果是管理员直接返回
            permissions.add("admin");
        } else {
            List<RoleDO> roles = roleMapper.selectRoleByUserId(user.getId());
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
        return roleMapper.countSuperLevelInUserIds(levelOfCurrentUserMaxRole, userIds) > 0 ? true : false;
    }

    /**
     * 清理缓存
     *
     * @param roleId
     * @param users
     */
    private void delCaches(Long roleId, List<UserDO> users) {
        users = CollectionUtil.isEmpty(users) ? userMapper.selectByRoleId(roleId) : users;
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
