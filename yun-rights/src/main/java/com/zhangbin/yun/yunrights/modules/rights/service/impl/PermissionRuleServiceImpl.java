package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.mybatis.page.PageQueryHelper;
import com.zhangbin.yun.common.utils.io.FileUtil;
import com.zhangbin.yun.yunrights.modules.rights.datarights.rule.RuleManager;
import com.zhangbin.yun.yunrights.modules.rights.mapper.PermissionRuleMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.RuleQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.PermissionRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionRuleServiceImpl implements PermissionRuleService {

    private final PermissionRuleMapper ruleMapper;

    @Override
    public PermissionRuleDO queryById(Long id) {
        return ruleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<List<PermissionRuleDO>> queryAllByCriteria(RuleQueryCriteria criteria) {
        Page<PermissionRuleDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, ruleMapper);
        PageInfo<List<PermissionRuleDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<PermissionRuleDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<PermissionRuleDO> queryAllByCriteriaWithNoPage(RuleQueryCriteria criteria) {
        return CollectionUtil.list(false, ruleMapper.selectAllByCriteria(criteria));
    }

    @Override
    public void create(PermissionRuleDO rule) {
        ruleMapper.insert(rule);
        RuleManager.refreshCache();
    }

    @Override
    public void update(PermissionRuleDO rule) {
        ruleMapper.updateByPrimaryKeySelective(rule);
        RuleManager.refreshCache();
    }

    @Override
    public void deleteById(Long id) {
        ruleMapper.deleteByPrimaryKey(id);
        RuleManager.refreshCache();
    }

    @Override
    public void download(List<PermissionRuleDO> permissionRuleList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.ofNullable(permissionRuleList).orElseGet(ArrayList::new).stream().map(PermissionRuleDO::toLinkedMap).collect(Collectors.toList()), response);
    }
}
