package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.RuleQueryCriteria;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface PermissionRuleService extends PageService<RuleQueryCriteria, PermissionRuleDO>{

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    PermissionRuleDO queryById(Long id);

    /**
     * 不分页查询满足条件的数据
     *
     * @param criteria 条件
     * @return /
     */
    List<PermissionRuleDO> queryAllByCriteriaWithNoPage(RuleQueryCriteria criteria);

    /**
     * 新增规则
     *
     * @param rule /
     */
    void create(PermissionRuleDO rule);

    /**
     * 编辑规则
     *
     * @param rule /
     */
    void update(PermissionRuleDO rule);

    /**
     * 删除规则
     *
     * @param id /
     */
    void deleteById(Long id);

    /**
     * 导出数据
     *
     * @param permissionRuleList 待导出的数据
     * @param response   /
     * @throws IOException /
     */
    void download(List<PermissionRuleDO> permissionRuleList, HttpServletResponse response) throws IOException;


}
