package com.zhangbin.yun.sys.modules.rights.controller;

import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.web.response.ResponseData;
import com.zhangbin.yun.sys.modules.logging.annotation.Logging;
import com.zhangbin.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.sys.modules.rights.model.criteria.RuleQueryCriteria;
import com.zhangbin.yun.sys.modules.rights.service.PermissionRuleService;

import static com.zhangbin.yun.common.web.response.ResponseUtil.success;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Api(tags = "系统：数据权限管理")
@RestController
@RequestMapping("/yun/rule")
@RequiredArgsConstructor
public class PermissionRuleController {

    private final PermissionRuleService ruleService;

    @Logging("导出规则数据")
    @ApiOperation("导出规则数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rule:list')")
    public void download(RuleQueryCriteria criteria, HttpServletResponse response) throws IOException {
        ruleService.downloadExcel(ruleService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询规则")
    @ApiOperation("根据ID查询规则")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('rule:list')")
    public ResponseEntity<ResponseData<PermissionRuleDO>> query(@PathVariable Long id) {
        return success(ruleService.queryById(id));
    }

    @Logging("根据条件查询规则")
    @ApiOperation("根据条件查询规则")
    @GetMapping
    @PreAuthorize("@el.check('rule:list')")
    public ResponseEntity<ResponseData<PageInfo<List<PermissionRuleDO>>>> queryByCriteria(@Validated RuleQueryCriteria criteria) {
        return success(ruleService.queryByCriteria(criteria));
    }

    @Logging("新增规则")
    @ApiOperation("新增规则")
    @PostMapping
    @PreAuthorize("@el.check('rule:add')")
    public ResponseEntity<ResponseData<Void>> create(@Validated(PermissionRuleDO.Create.class) @RequestBody PermissionRuleDO rule) {
        ruleService.create(rule);
        return success();
    }

    @Logging("修改规则")
    @ApiOperation("修改规则")
    @PutMapping
    @PreAuthorize("@el.check('rule:edit')")
    public ResponseEntity<ResponseData<Void>> update(@Validated(PermissionRuleDO.Update.class) @RequestBody PermissionRuleDO rule) {
        ruleService.update(rule);
        return success();
    }

    @Logging("删除规则")
    @ApiOperation("删除规则")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('rule:del')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable Long id) {
        ruleService.deleteById(id);
        return success();
    }
}
