package com.hlcy.yun.sys.modules.rights.service.impl;

import com.hlcy.yun.sys.modules.rights.common.excel.CollectChildren;
import com.hlcy.yun.sys.modules.rights.common.tree.TreeBuilder;
import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.DeptQueryCriteria;
import com.hlcy.yun.sys.modules.rights.model.dto.DeptDTO;
import com.hlcy.yun.sys.modules.rights.service.GroupService;
import com.hlcy.yun.common.utils.io.FileUtil;
import com.hlcy.yun.sys.modules.rights.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dept")
class DeptServiceImpl implements DeptService {
    // 对组的操作委派给 GroupService 处理
    private final GroupService groupService;

    @Override
    public List<DeptDTO> queryAllByCriteriaWithNoPage(DeptQueryCriteria criteria) {
        return Optional.ofNullable(groupService.queryAllByCriteriaWithNoPage(criteria))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DeptDTO queryById(Long id) {
        GroupDO group = groupService.queryById(id);
        return group == null ? null : group.toDept();
    }

    @Override
    public List<DeptDTO> queryByPid(Long pid) {
        return Optional.ofNullable(groupService.queryByPid(pid))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    public List<DeptDTO> queryAncestorAndSiblingOfDepts(Set<Long> groupIds) {
        return Optional.ofNullable(groupService.queryAncestorAndSibling(groupIds))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    public void create(DeptDTO dept) {
        groupService.create(dept.toGroup());
    }

    @Override
    public void update(DeptDTO dept) {
        groupService.update(dept.toGroup());
    }

    @Override
    public void deleteByDeptIds(Set<Long> deptIds) {
        groupService.deleteByIds(deptIds);
    }

    @Override
    public List<DeptDTO> buildDeptTree(Collection<DeptDTO> depts) {
        return new TreeBuilder<DeptDTO>().buildTree(depts);
    }

    @Override
    public void downloadExcel(Collection<DeptDTO> collection, HttpServletResponse response) {
        List<DeptDTO> deptsSorted = new ArrayList<>();
        buildDeptTree(collection).forEach(new CollectChildren<>(deptsSorted));
        FileUtil.downloadExcel(deptsSorted.stream().map(DeptDTO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public Boolean isAssociatedUser(Set<Long> deptIds) {
        return groupService.isAssociatedUser(deptIds);
    }

//    private void checkOperationalRights(){// TODO
//        String currentUsername = SecurityUtils.getCurrentUsername();
//        UserDO currentUser = userService.queryByUsername(currentUsername);
//        Assert.isTrue(currentUser.isAdmin(), "你没有操作权限!");
//    }
}
