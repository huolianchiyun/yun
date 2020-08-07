package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SetUtils;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.GroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;

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


        return null;
    }

    @Override
    public List<GroupDO> queryByRoleId(Long roleId) {
        return CollectionUtil.list(false, groupMapper.selectByRoleId(roleId));
    }

    @Override
    public void createGroup(GroupDO group) {
        groupMapper.insert(group);
    }

    @Override
    public void updateDept(GroupDO group) {
        groupMapper.updateByPrimaryKeySelective(group);
    }

    @Override
    public void deleteByGroupIds(Set<Long> groupIds) {
        groupMapper.batchDeleteByIds(groupIds);
    }

    @Override
    public List<GroupDO> buildGroupTree(Collection<GroupDO> groups) {
        return TreeBuilder.build().buildTree(groups);
    }

    @Override
    public void download(List<GroupDO> depts, HttpServletResponse response) throws IOException {
        List<GroupDO> groupSorted = new ArrayList<>();
        buildGroupTree(depts).forEach(new CollectChildren<>(groupSorted));
        FileUtil.downloadExcel(groupSorted.stream().map(GroupDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public void verification(Set<GroupDO> groups) {

    }
}
