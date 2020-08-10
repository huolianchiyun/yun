package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.RedisUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SetUtils;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.*;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final UserGroupMapper userGroupMapper;
    private final GroupRoleMapper groupRoleMapper;
    private final UserMapper userMapper;
    private final RedisUtils redisUtils;

    @Override
    public List<GroupDO> queryAllByCriteriaWithNoPage(GroupQueryCriteria criteria) {
        return SetUtils.toListWithSorted(groupMapper.selectAllByCriteria(criteria), GroupDO::compareTo);
    }

    @Override
    public GroupDO queryById(Long id) {
        return groupMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<GroupDO> queryByPid(Long pid) {
        return CollectionUtil.list(false, groupMapper.selectByPid(pid));
    }

    @Override
    public List<GroupDO> queryAncestorAndSiblingOfDepts(Set<Long> groupIds) {
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
    public List<GroupDO> queryByRoleId(Long roleId) {
        return CollectionUtil.list(false, groupMapper.selectByRoleId(roleId));
    }

    @Override
    public void createGroup(GroupDO group) {
        group.setId(null);
        groupMapper.insert(group);
        // 清理缓存
        redisUtils.del("group::pid:" + (group.getPid()));
    }

    @Override
    public void updateGroup(GroupDO updatingGroup) {
        Assert.isTrue(updatingGroup.getId().equals(updatingGroup.getPid()), "上级不能为自己!");
        GroupDO groupDb = groupMapper.selectByPrimaryKey(updatingGroup.getId());
        Assert.isNull(groupDb, "修改的组不存在！");
        updatingGroup.setOldPid(groupDb.getPid());
        groupMapper.updateByPrimaryKeySelective(updatingGroup);
        // 清理缓存
        delCaches(CollectionUtil.newHashSet(updatingGroup));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByGroupIds(Set<Long> groupIds) {
        // 获取要删除组及子孙组
        Set<GroupDO> posterityGroupWithSelf = new HashSet<>(getPosterityMenusWithSelf(groupIds));
        Set<Long> deletingGroupIds = posterityGroupWithSelf.stream().map(GroupDO::getId).collect(Collectors.toSet());
        Assert.isTrue(isAssociatedUser(deletingGroupIds), "将要删除的组或其子组与用户存在关联，请解除关联关系后，再尝试！");
        groupMapper.deleteByIds(deletingGroupIds);
        groupRoleMapper.deleteByGroupIds(deletingGroupIds);
        userGroupMapper.deleteByGroupIds(deletingGroupIds);
        delCaches(posterityGroupWithSelf);
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
     * 清理缓存
     */
    public void delCaches(Set<GroupDO> groups){
        Set<Long> groupIds = groups.stream().map(GroupDO::getId).collect(Collectors.toSet());
        Set<UserDO> users = userMapper.selectByGroupIds(groupIds);
        // 删除数据权限
        redisUtils.delByKeys("data::user:", users.stream().map(UserDO::getId).collect(Collectors.toSet()));
        groups.forEach(e->{
            redisUtils.del("group::id:" + e.getId());
            redisUtils.del("group::pid:" + e.getOldPid());
            redisUtils.del("group::pid:" + e.getPid());
        });
    }
}
