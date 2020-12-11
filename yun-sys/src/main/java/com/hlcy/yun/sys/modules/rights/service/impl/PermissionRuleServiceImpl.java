package com.hlcy.yun.sys.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.hlcy.yun.sys.modules.rights.model.criteria.RuleQueryCriteria;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.mybatis.page.PageQueryHelper;
import com.hlcy.yun.sys.modules.rights.datarights.rule.RuleManager;
import com.hlcy.yun.sys.modules.rights.mapper.PermissionRuleMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import com.hlcy.yun.sys.modules.rights.service.PermissionRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
class PermissionRuleServiceImpl implements PermissionRuleService {

    private final PermissionRuleMapper ruleMapper;

    @Override
    public PermissionRuleDO queryById(Long id) {
        return ruleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<List<PermissionRuleDO>> queryByCriteria(RuleQueryCriteria criteria) {
        Page<PermissionRuleDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, ruleMapper);
        PageInfo<List<PermissionRuleDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<PermissionRuleDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<PermissionRuleDO> queryAllByCriteriaWithNoPage(RuleQueryCriteria criteria) {
        return CollectionUtil.list(false, ruleMapper.selectByCriteria(criteria));
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
}
