package com.hlcy.yun.sys.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.hlcy.yun.common.mybatis.page.PageQueryHelper;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.common.utils.collect.SetUtils;
import com.hlcy.yun.sys.modules.rights.common.excel.CollectChildren;
import com.hlcy.yun.sys.modules.rights.common.tree.TreeBuilder;
import com.hlcy.yun.sys.modules.rights.mapper.DeptMapper;
import com.hlcy.yun.sys.modules.rights.mapper.UserMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.DeptDO;
import com.hlcy.yun.common.utils.io.FileUtil;
import com.hlcy.yun.sys.modules.rights.model.criteria.DeptQueryCriteria;
import com.hlcy.yun.sys.modules.rights.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.hlcy.yun.sys.modules.common.xcache.CacheKey.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dept")
class DeptServiceImpl implements DeptService {
    private final DeptMapper deptMapper;
    private final UserMapper userMapper;
    private RedisUtils redisUtils;

    @Override
    public PageInfo<List<DeptDO>> queryByCriteria(DeptQueryCriteria criteria) {
        Page<DeptDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, deptMapper);
        PageInfo<List<DeptDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<DeptDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<DeptDO> queryAllByCriteriaWithNoPage(DeptQueryCriteria criteria) {
        return SetUtils.toListWithSorted(deptMapper.selectByCriteria(criteria), DeptDO::compareTo);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public DeptDO queryById(Long id) {
        return deptMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DeptDO> queryByPid(Long pid) {
        return SetUtils.toListWithSorted(deptMapper.selectByPid(pid), DeptDO::compareTo);
    }

    @Override
    public List<DeptDO> queryAncestorAndSibling(Set<Long> deptIds) {
        if (CollectionUtil.isEmpty(deptIds)) {
            return queryByPid(null);
        }
        // 获取所有部门
        Set<DeptDO> allDepts = deptMapper.selectByCriteria(null);
        // 删掉 deptIds 的直接子部门，使其连接断路
        List<DeptDO> depts = allDepts.stream().filter(e -> !deptIds.contains(e.getPid())).collect(Collectors.toList());
        return buildDeptTree((depts));
    }

    @Override
    public void create(DeptDO dept) {
        deptMapper.insert(dept);
    }

    @Override
    public void update(DeptDO dept) {
        deptMapper.updateByPrimaryKeySelective(dept);
    }

    @Override
    public void deleteByDeptIds(Set<Long> deptIds) {
        // 获取要删除组及子孙组
        Set<DeptDO> posterityGroupWithSelf = new HashSet<>(getPosterityMenusWithSelf(deptIds));
        Set<Long> deletingDeptIds = posterityGroupWithSelf.stream().map(DeptDO::getId).collect(Collectors.toSet());
        Assert.isTrue(!isAssociatedUser(deletingDeptIds), "将要删除的部门或其子部门与用户存在关联，请解除关联关系后，再尝试！");
        deptMapper.deleteByIds(deletingDeptIds);
        clearCaches(posterityGroupWithSelf);
    }

    @Override
    public Boolean isAssociatedUser(Set<Long> deptIds) {
        Assert.notEmpty(deptIds, "检验部门是否关联用户， dept id must not empty.");
        return deptMapper.countAssociatedUser(deptIds) > 0;
    }


    @Override
    public List<DeptDO> buildDeptTree(Collection<DeptDO> depts) {
        return new TreeBuilder<DeptDO>().buildTree(depts);
    }

    @Override
    public void downloadExcel(Collection<DeptDO> collection, HttpServletResponse response) {
        List<DeptDO> deptsSorted = new ArrayList<>();
        buildDeptTree(collection).forEach(new CollectChildren<>(deptsSorted));
        FileUtil.downloadExcel(deptsSorted.stream().map(DeptDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    /**
     * 获取部门集合的子孙部门及自己
     *
     * @param deptIds /
     */
    private List<DeptDO> getPosterityMenusWithSelf(Set<Long> deptIds) {
        Set<DeptDO> allGroups = deptMapper.selectAll();
        List<DeptDO> tree = new TreeBuilder<DeptDO>().buildTree(allGroups, deptIds);
        List<DeptDO> groupsSorted = new ArrayList<>();
        tree.forEach(new CollectChildren<>(groupsSorted));
        return groupsSorted;
    }

//    private void checkOperationalRights(){// TODO
//        String currentUsername = SecurityUtils.getCurrentUsername();
//        UserDO currentUser = userService.queryByUsername(currentUsername);
//        Assert.isTrue(currentUser.isAdmin(), "你没有操作权限!");
//    }

    // TODO 考虑加缓存
    private void clearCaches(Set<DeptDO> depts) {
        if (CollectionUtil.isEmpty(depts)) return;
        depts.forEach(e -> {
            redisUtils.del(DEPT_ID + e.getId(),
                    DEPT_PID + e.getOldPid(), DEPT_PID + e.getPid());
        });
    }
}
