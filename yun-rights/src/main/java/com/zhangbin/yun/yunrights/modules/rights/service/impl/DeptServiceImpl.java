package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import com.zhangbin.yun.yunrights.modules.common.utils.FileUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.tree.TreeBuilder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DeptQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
import com.zhangbin.yun.yunrights.modules.rights.service.DeptService;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dept")
public class DeptServiceImpl implements DeptService {

    private final GroupService groupService;  // 对组的操作委派给 GroupService 处理
    private final UserService userService;

    @Override
    public List<DeptDTO> queryAllByCriteriaWithNoPage(DeptQueryCriteria criteria) {
        return Optional.of(groupService.queryAllByCriteriaWithNoPage(criteria))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    public DeptDTO queryById(Long id) {
        return groupService.queryById(id).toDept();
    }

    @Override
    public List<DeptDTO> queryByPid(Long pid) {
        return Optional.of(groupService.queryByPid(pid))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    public List<DeptDTO> queryAncestorAndSiblingOfDepts(Set<Long> groupIds) {
        return Optional.of(groupService.queryAncestorAndSiblingOfGroups(groupIds))
                .orElseGet(ArrayList::new).stream().map(GroupDO::toDept).collect(Collectors.toList());
    }

    @Override
    public void createDept(DeptDTO dept) {
        groupService.createGroup(dept.toGroup());
    }

    @Override
    public void updateDept(DeptDTO dept) {
        groupService.updateGroup(dept.toGroup());
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
    public void download(List<DeptDTO> depts, HttpServletResponse response) throws IOException {
        List<DeptDTO> deptsSorted = new ArrayList<>();
        buildDeptTree(depts).forEach(new CollectChildren<>(deptsSorted));
        FileUtil.downloadExcel(deptsSorted.stream().map(DeptDTO::toLinkedMap).collect(Collectors.toList()), response);
    }

    @Override
    public Boolean isAssociatedUser(Set<Long> deptIds) {
        return groupService.isAssociatedUser(deptIds);
    }

    private void checkOperationalRights(){
        String currentUsername = SecurityUtils.getCurrentUsername();
        UserDO currentUser = userService.queryByUserName(currentUsername);
        Assert.isTrue(!currentUser.isAdmin(), "你没有操作权限，尽");
    }
}
