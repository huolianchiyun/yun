package com.yun.sys.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.yun.sys.modules.rights.common.tree.TreeBuilder;
import com.yun.sys.modules.rights.mapper.*;
import com.yun.sys.modules.rights.model.$do.GroupDO;
import com.yun.sys.modules.rights.model.$do.GroupMenuDO;
import com.yun.sys.modules.rights.model.$do.UserDO;
import com.yun.sys.modules.rights.model.$do.UserGroupDO;
import com.yun.common.spring.redis.RedisUtils;
import com.yun.common.spring.security.SecurityUtils;
import com.yun.common.utils.io.FileUtil;
import com.yun.common.utils.collect.SetUtils;
import com.yun.common.utils.str.StringUtils;
import com.yun.common.page.PageInfo;
import com.yun.common.mybatis.page.PageQueryHelper;
import static com.yun.sys.modules.common.xcache.CacheKey.*;
import static com.yun.sys.modules.rights.common.constant.RightsConstants.DICT_SUFFIX;
import com.yun.sys.modules.rights.common.excel.CollectChildren;
import com.yun.sys.modules.rights.model.criteria.GroupQueryCriteria;
import com.yun.sys.modules.rights.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = GROUP)
class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final GroupMenuMapper groupMenuMapper;
    private final ApiRightsMapper apiRightsMapper;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public PageInfo<List<GroupDO>> queryByCriteria(GroupQueryCriteria criteria) {
        Page<GroupDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, groupMapper);
        PageInfo<List<GroupDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<GroupDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<GroupDO> queryAllByCriteriaWithNoPage(GroupQueryCriteria criteria) {
        return SetUtils.toListWithSorted(groupMapper.selectByCriteria(criteria), GroupDO::compareTo);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public GroupDO queryById(Long id) {
        return groupMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<GroupDO> queryByIds(Set<Long> ids) {
        return new ArrayList<>(Optional.ofNullable(groupMapper.selectByPrimaryKeys(ids)).orElseGet(HashSet::new));
    }

    @Override
    @Cacheable(value = BIND_USER + GROUP, key = "'" + UserIdKey + "' + #p0")
    public List<GroupDO> queryByUserId(Long userId) {
        return new ArrayList<>(Optional.ofNullable(groupMapper.selectGroupByUserId(userId)).orElseGet(HashSet::new));
    }

    @Override
    @Cacheable(value = BIND_USER + GROUP, key = "'" + UsernameKey + "' + #p0")
    public Set<String> queryGroupCodesByUsername(String username) {
        return Optional.ofNullable(groupMapper.selectGroupCodesByUsername(username)).orElseGet(HashSet::new);
    }

    @Override
    public Set<GroupDO> queryByMenuIds(Set<Long> menuIds) {
        return Optional.ofNullable(groupMapper.selectByMenuIds(menuIds)).orElseGet(HashSet::new);
    }

    @Override
    @Cacheable(key = "'pid:' + #p0")
    public List<GroupDO> queryByPid(Long pid) {
        return SetUtils.toListWithSorted(groupMapper.selectByPid(pid), GroupDO::compareTo);
    }

    @Override
    public List<GroupDO> queryAncestorAndSibling(Set<Long> groupIds) {
        if (CollectionUtil.isEmpty(groupIds)) {
            return queryByPid(null);
        }
        // 获取所有组
        Set<GroupDO> allGroups = groupMapper.selectByCriteria(null);
        // 删掉 groupIds 的直接子组，使其连接断路
        List<GroupDO> groups = allGroups.stream().filter(e -> !groupIds.contains(e.getPid())).collect(Collectors.toList());
        return buildGroupTree((groups));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {@CacheEvict(key = "'id:' + #p0.pid"), @CacheEvict(key = "'pid:' + #p0.pid")})
    public void create(GroupDO group) {
        // 校验组长信息，若不存在，将创建人设置为组长
        checkOperationalRights(group);
        setGroupMasterFor(group);
        groupMapper.insert(group);
        // 更新组编码
        groupMapper.updateGroupCodeById(generateGroupCode(group), group.getId());
        updateAssociatedMenu(group, true);
        updateAssociatedUser(group, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupDO updatingGroup) {
        Assert.isTrue(!updatingGroup.getId().equals(updatingGroup.getPid()), "上级不能为自己!");
        GroupDO groupDB = groupMapper.selectByPrimaryKey(updatingGroup.getId());
        Assert.notNull(groupDB, "修改的组不存在！");
        if (!groupDB.getPid().equals(updatingGroup.getPid())) {
            updatingGroup.setGroupCode(generateGroupCode(updatingGroup));
        }
        checkOperationalRights(updatingGroup);
        updatingGroup.setOldPid(groupDB.getPid());
        groupMapper.updateByPrimaryKeySelective(updatingGroup);
        updateAssociatedMenu(updatingGroup, false);
        updateAssociatedUser(updatingGroup, false);
        // 清理缓存
        clearCaches(CollectionUtil.newHashSet(updatingGroup));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        // 获取要删除组及子孙组
        Set<GroupDO> posterityGroupWithSelf = new HashSet<>(getPosterityMenusWithSelf(ids));
        Set<Long> deletingGroupIds = posterityGroupWithSelf.stream().map(GroupDO::getId).collect(Collectors.toSet());
        Assert.isTrue(!isAssociatedUser(deletingGroupIds), "将要删除的组或其子组与用户存在关联，请解除关联关系后，再尝试！");
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
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
    public List<GroupDO> buildGroupTree(Collection<GroupDO> groups) {
        return new TreeBuilder<GroupDO>().buildTree(groups);
    }

    @Override
    public void downloadExcel(Collection<GroupDO> collection, HttpServletResponse response){
        List<GroupDO> groupSorted = new ArrayList<>();
        buildGroupTree(collection).forEach(new CollectChildren<>(groupSorted));
        FileUtil.downloadExcel(groupSorted.stream().map(GroupDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public Boolean isAssociatedUser(Set<Long> groupIds) {
        Assert.notEmpty(groupIds, "检验组是否关联用户， group id must not empty.");
        return groupMapper.countAssociatedUser(groupIds) > 0;
    }

    @Override
    @Cacheable(value = BIND_USER + GROUP, key = "'auth:" + UserIdKey + "' + #user.id")
    public List<GrantedAuthority> getGrantedAuthorities(UserDO user) {
        Set<String> permissions = new HashSet<>(1);
        if (user.getAdmin()) {  // 如果是管理员直接返回
            permissions.add("all");
        } else {
            permissions = new ArrayList<>(Optional.ofNullable(apiRightsMapper.selectAuthorizationsByUserId(user.getId())).orElseGet(HashSet::new)).stream()
                    .filter(StringUtils::isNotEmpty)
                    .flatMap(authorization -> Arrays.stream(authorization.contains(",") ? authorization.split(",") : new String[]{authorization}))
                    .collect(Collectors.toSet());
        }
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public void clearCaches(Set<GroupDO> groups) {
        if (CollectionUtil.isEmpty(groups)) return;
        groups.forEach(e -> {
            redisUtils.del(GROUP_ID + e.getId(),
                    GROUP_PID + e.getOldPid(), GROUP_PID + e.getPid(),
                    GROUP_BIND_MENU + e.getId() + true, GROUP_BIND_MENU + e.getId() + false);
        });
    }

    private void checkOperationalRights(GroupDO group) {
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
        checkOperationalRights(group, currentUser);
    }

    private void checkOperationalRights(GroupDO group, UserDO currentUser) {
        if (currentUser.getAdmin()) {  // 管理员直接放行
            return;
        }
        Set<GroupDO> currentUserGroups = groupMapper.selectGroupByUserId(currentUser.getId());
        String groupCode = group.getGroupCode();
        if (Objects.nonNull(group.getId())) {
            // 修改组
            // 若修改的组是当前用户所属组或其子组且是用户所属组或其父组的群主，则放行
            Assert.isTrue(currentUserGroups.stream()
                            .filter(e -> groupCode.startsWith(e.getGroupCode()))
                            .anyMatch(e -> currentUser.getUsername().equals(e.getGroupMaster())),
                    "你不是将要修改组或其父组的群主，故没有操作权限！");
        } else {
            // 创建组
            // 若创建的组是当前用户所属组的子组且用户是父组群主，则放行
            Assert.isTrue(currentUserGroups.stream()
                            .filter(e -> groupCode.startsWith(e.getGroupCode()) && !groupCode.equals(e.getGroupCode()))
                            .anyMatch(e -> currentUser.getUsername().equals(e.getGroupMaster())),
                    "你不是将要创建组的父组的群主，故没有操作权限！");
        }
    }

    /**
     * 获取组集合的子孙组及自己
     *
     * @param groupIds 组集合
     */
    private List<GroupDO> getPosterityMenusWithSelf(Set<Long> groupIds) {
        Set<GroupDO> allGroups = groupMapper.selectAll();
        List<GroupDO> tree = new TreeBuilder<GroupDO>().buildTree(allGroups, groupIds);
        List<GroupDO> groupsSorted = new ArrayList<>();
        tree.forEach(new CollectChildren<>(groupsSorted));
        return groupsSorted;
    }

    /**
     * 修改组关联的菜单
     * 注意： 使用前要进行操作权限校验，参考： {@code  GroupServiceImpl#checkOperationalRights(updatingGroup)}
     *
     * @param group   创建或修改的组
     * @param isCreat 是否是创建组
     */
    private void updateAssociatedMenu(GroupDO group, boolean isCreat) {
        Set<GroupMenuDO> groupMenus = Optional.ofNullable(group.getMenus()).orElseGet(HashSet::new)
                .stream().map(e -> new GroupMenuDO(group.getId(), e.getId())).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(groupMenus)) {
            if (!isCreat) {
                // 清除之前绑定的菜单
                groupMenuMapper.deleteByGroupIds(CollectionUtil.newHashSet(group.getId()));
            }
            // 重新绑定新的菜单
            groupMenuMapper.batchInsert(groupMenus);
        }
    }

    /**
     * 修改组关联的用户
     *
     * @param group    创建或修改的组
     * @param isCreate 是否是创建组
     */
    private void updateAssociatedUser(GroupDO group, boolean isCreate) {
        if (CollectionUtil.isNotEmpty(group.getUsers())) {
            Set<UserGroupDO> userGroups = group.getUsers().stream()
                    .map(e -> new UserGroupDO(e.getId(), group.getId()))
                    .collect(Collectors.toSet());
            if (!isCreate) {
                userGroupMapper.deleteByGroupIds(CollectionUtil.newHashSet(group.getId()));
            }
            userGroupMapper.batchInsert(userGroups);
        }
    }

    private String generateGroupCode(GroupDO group) {
        String pGroupCode = null;
        if (null != group.getPid() && group.getPid() != 0) {
            pGroupCode = groupMapper.selectGroupCodeByIdForUpdate(group.getPid());
        }
        if (StringUtils.isNotEmpty(pGroupCode)) {
            return pGroupCode + ":" + group.getId();
        } else {
            String groupType = group.getGroupType();
            Assert.isTrue(StringUtils.isNotBlank(groupType) && !groupType.trim().equals(DICT_SUFFIX), "组类型必填，不能为空！");
            String prefix = groupType.endsWith(DICT_SUFFIX) ? groupType.substring(0, groupType.lastIndexOf(DICT_SUFFIX)) : groupType + "::";
            return prefix + group.getId();
        }
    }

    private void setGroupMasterFor(GroupDO group) {
        String groupMaster = group.getGroupMaster();
        String currentUsername = SecurityUtils.getCurrentUsername();
        if (StringUtils.isBlank(groupMaster)) {
            group.setGroupMaster(currentUsername);
        } else {
            Assert.notNull(userMapper.selectByUsername(groupMaster), "指定的组长（" + groupMaster + "）不存在!");
        }
    }
}
