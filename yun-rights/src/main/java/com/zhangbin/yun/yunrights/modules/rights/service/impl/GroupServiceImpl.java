package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.*;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupMenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import com.zhangbin.yun.yunrights.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final GroupMenuMapper groupMenuMapper;
    private final UserCacheClean userCacheClean;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public PageInfo<List<GroupDO>> queryAllByCriteria(GroupQueryCriteria criteria) {
        Page<GroupDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, groupMapper);
        PageInfo<List<GroupDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<GroupDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<GroupDO> queryAllByCriteriaWithNoPage(GroupQueryCriteria criteria) {
        return SetUtils.toListWithSorted(groupMapper.selectAllByCriteria(criteria), GroupDO::compareTo);
    }

    @Override
    public GroupDO queryById(Long id) {
        return Optional.of(groupMapper.selectByPrimaryKey(id)).orElseGet(GroupDO::new);
    }

    @Override
    public List<GroupDO> queryByIds(Set<Long> ids) {
        return new ArrayList<>(Optional.of(groupMapper.selectByPrimaryKeys(ids)).orElseGet(HashSet::new));
    }

    @Override
    public List<GroupDO> queryByUserId(Long userId) {
        return new ArrayList<>(Optional.of(groupMapper.selectByUserId(userId)).orElseGet(HashSet::new));
    }

    @Override
    public Set<GroupDO> queryByMenuIds(Set<Long> menuIds) {
        return Optional.of(groupMapper.selectByMenuIds(menuIds)).orElseGet(HashSet::new);
    }

    @Override
    public List<GroupDO> queryByPid(Long pid) {
        return CollectionUtil.list(false, groupMapper.selectByPid(pid));
    }

    @Override
    public List<GroupDO> queryAncestorAndSiblingOfGroups(Set<Long> groupIds) {
        if (CollectionUtil.isEmpty(groupIds)) {
            return queryByPid(null);
        }
        // 获取所有组
        Set<GroupDO> allGroups = groupMapper.selectAllByCriteria(null);
        // 删掉 groupIds 的直接子组，使其连接断路
        List<GroupDO> groups = allGroups.stream().filter(e -> !groupIds.contains(e.getPid())).collect(Collectors.toList());
        return buildGroupTree((groups));
    }

    @Override
    public void createGroup(GroupDO group) {
        group.setId(null);
        checkOperationalRights(group);
        groupMapper.insert(group);
        // 清理缓存
        redisUtils.del("group::pid:" + (group.getPid()));
    }

    @Override
    public void updateGroup(GroupDO updatingGroup) {
        Assert.isTrue(updatingGroup.getId().equals(updatingGroup.getPid()), "上级不能为自己!");
        GroupDO groupDB = groupMapper.selectByPrimaryKey(updatingGroup.getPid());
        Assert.isNull(groupDB, "修改的组不存在！");
        checkOperationalRights(updatingGroup);
        updatingGroup.setOldPid(groupDB.getPid());
        groupMapper.updateByPrimaryKeySelective(updatingGroup);
        updateAssociatedMenu(updatingGroup);
        // 清理缓存
        clearCaches(CollectionUtil.newHashSet(updatingGroup));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        // 获取要删除组及子孙组
        Set<GroupDO> posterityGroupWithSelf = new HashSet<>(getPosterityMenusWithSelf(ids));
        Set<Long> deletingGroupIds = posterityGroupWithSelf.stream().map(GroupDO::getId).collect(Collectors.toSet());
        Assert.isTrue(isAssociatedUser(deletingGroupIds), "将要删除的组或其子组与用户存在关联，请解除关联关系后，再尝试！");
        UserDO currentUser = userMapper.selectByUserName(SecurityUtils.getCurrentUsername());
        Set<GroupDO> deletingGroups = posterityGroupWithSelf.stream().filter(e -> ids.contains(e.getId())).collect(Collectors.toSet());
        deletingGroups.forEach(e -> {
            checkOperationalRights(e, currentUser);
        });
        groupMapper.deleteByIds(deletingGroupIds);
        groupMenuMapper.deleteByGroupIds(deletingGroupIds);
        userGroupMapper.deleteByGroupIds(deletingGroupIds);
        clearCaches(posterityGroupWithSelf);
    }

    @Override
    public void addUsersIntoGroup(Set<Long> userIds, Long groupId) {
        GroupDO group = groupMapper.selectByPrimaryKey(groupId);
        checkOperationalRights(group);
        userGroupMapper.addUsersIntoGroup(userIds, groupId);
    }

    @Override
    public List<GroupDO> buildGroupTree(Collection<GroupDO> groups) {
        return new TreeBuilder<GroupDO>().buildTree(groups);
    }

    @Override
    public void download(List<GroupDO> depts, HttpServletResponse response) throws IOException {
        List<GroupDO> groupSorted = new ArrayList<>();
        buildGroupTree(depts).forEach(new CollectChildren<>(groupSorted));
        FileUtil.downloadExcel(groupSorted.stream().map(GroupDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public Boolean isAssociatedUser(Set<Long> groupIds) {
        return groupMapper.countAssociatedUser(groupIds) > 0;
    }

    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> getGrantedAuthorities(UserDO user) {
        Set<String> permissions = new HashSet<>(1);
        if (user.isAdmin()) {  // 如果是管理员直接返回
            permissions.add("admin");
        } else {
            List<GroupDO> roles = new ArrayList<>(Optional.of(groupMapper.selectByUserId(user.getId()))
                    .orElseGet(HashSet::new));
            permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                    .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                    .map(MenuDO::getPermission).collect(Collectors.toSet());
        }
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void checkOperationalRights(GroupDO group) {
        UserDO currentUser = userMapper.selectByUserName(SecurityUtils.getCurrentUsername());
        checkOperationalRights(group, currentUser);
    }

    private void checkOperationalRights(GroupDO group, UserDO currentUser) {
        if (currentUser.isAdmin()) {  // 管理员直接放行
            return;
        }
        Set<GroupDO> currentUserGroups = groupMapper.selectByUserId(currentUser.getId());
        // 当前用户是不是该组群主，是直接放行
        List<GroupDO> groupFiltered = currentUserGroups.stream().filter(group::equals).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(groupFiltered)) {
            if (groupFiltered.get(0).getGroupMaster().equals(currentUser.getUserName())) return;
        }
        // 操作的组是不是当前用户的所属组的子组
        String groupCode = group.getGroupCode();
        Assert.isTrue(currentUserGroups.stream().noneMatch((e -> groupCode.startsWith(e.getGroupCode()))),
                "权限不够，你只能对你所属的组或子组进行操作！");
    }

    /**
     * 获取组集合的子孙组及自己
     *
     * @param groupIds 组集合
     */
    private List<GroupDO> getPosterityMenusWithSelf(Set<Long> groupIds) {
        Set<GroupDO> allGroups = groupMapper.selectAllByCriteria(null);
        List<GroupDO> tree = new TreeBuilder<GroupDO>().buildTree(allGroups, groupIds);
        List<GroupDO> groupsSorted = new ArrayList<>();
        tree.forEach(new CollectChildren<>(groupsSorted));
        return groupsSorted;
    }

    /**
     * 修改组关联的菜单
     * 注意： 使用前要进行操作权限校验，参考： {@code  GroupServiceImpl#checkOperationalRights(updatingGroup)}
     *
     * @param group 修改的组
     */
    private void updateAssociatedMenu(GroupDO group) {
        Set<GroupMenuDO> groupMenus = Optional.of(group.getMenus()).orElseGet(HashSet::new)
                .stream().map(e -> new GroupMenuDO(group.getId(), e.getId())).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(groupMenus)) {
            // 清除之前绑定的菜单
            groupMenuMapper.deleteByGroupIds(CollectionUtil.newHashSet(group.getId()));
            // 重新绑定新的菜单
            groupMenuMapper.batchInsert(groupMenus);
        }
    }

    /**
     * 清理缓存
     */
    private void clearCaches(Set<GroupDO> groups) {
        if (CollectionUtil.isEmpty(groups)) return;
        Set<Long> groupIds = groups.stream().map(GroupDO::getId).collect(Collectors.toSet());
        Set<UserDO> users = userMapper.selectByGroupIds(groupIds);
        // 删除数据权限
        groups.forEach(e -> {
            Long groupId = e.getId();
            redisUtils.del("group::id:" + groupId);
            redisUtils.del("group::pid:" + e.getOldPid());
            redisUtils.del("group::pid:" + e.getPid());
            redisUtils.del(CacheKey.GROUP_ID + groupId);
        });
        if (CollectionUtil.isNotEmpty(users)) {
            redisUtils.delByKeys("data::user:", users.stream().map(UserDO::getId).collect(Collectors.toSet()));
            users.forEach(item -> userCacheClean.cleanUserCache(item.getUserName()));
            Set<Long> userIds = users.stream().map(UserDO::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATE_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.GROUP_AUTH, userIds);
        }
    }
}
