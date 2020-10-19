package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.common.web.response.ResponseData;

import static com.zhangbin.yun.common.web.response.ResponseUtil.success;

import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DeptQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
import com.zhangbin.yun.yunrights.modules.rights.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/yun/dept")
public class DeptController {

    private final DeptService deptService;

    @Logging("导出部门数据")
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws Exception {
        criteria.setPid(null);
        deptService.download(deptService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询部门")
    @ApiOperation("根据ID查询部门")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<ResponseData> query(@PathVariable Long id) {
        return success(deptService.queryById(id));
    }

    @Logging("根据条件查询部门")
    @ApiOperation("根据条件查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<ResponseData> query(DeptQueryCriteria criteria) {
        return success(deptService.queryAllByCriteriaWithNoPage(criteria));
    }

    @Logging("查询部门: 根据ID获取同级与上级数据")
    @ApiOperation("查询部门: 根据ID获取同级与上级数据")
    @PostMapping("/tree2me")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<ResponseData> queryAncestorAndSiblingOfDepts(@RequestBody Set<Long> deptIds) {
        return success(deptService.queryAncestorAndSiblingOfDepts(deptIds));
    }

    @Logging("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<ResponseData> create(@Validated(DeptDTO.Create.class) @RequestBody DeptDTO dept) {
        deptService.create(dept);
        return success();
    }

    @Logging("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity<ResponseData> update(@Validated(DeptDTO.Update.class) @RequestBody DeptDTO dept) {
        deptService.update(dept);
        return success();
    }

    @Logging("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<ResponseData> delete(@RequestBody Set<Long> deptIds) {
        deptService.deleteByDeptIds(deptIds);
        return success();
    }
}
