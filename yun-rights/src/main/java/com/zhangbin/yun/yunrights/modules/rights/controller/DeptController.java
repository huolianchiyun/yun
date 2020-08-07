package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import com.zhangbin.yun.yunrights.modules.common.utils.PageUtil;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DeptQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.DeptDTO;
import com.zhangbin.yun.yunrights.modules.rights.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
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

    @Logging("查询部门")
    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<ResponseData> query(DeptQueryCriteria criteria) {
        List<DeptDTO> depts = deptService.queryAllByCriteriaWithNoPage(criteria);
        return success(PageUtil.toPage(depts, depts.size()));
    }

    @Logging("查询部门")
    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/batch/family")
    @PreAuthorize("@el.check('user:list','dept:list')")
    public ResponseEntity<ResponseData> queryAncestorAndSiblingOfDepts(@RequestBody Set<Long> deptIds) {
        return success(deptService.queryAncestorAndSiblingOfDepts(deptIds));
    }

    @Logging("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResponseEntity<ResponseData> createDept(@RequestBody DeptDTO dept) {
        deptService.createDept(dept);
        return success();
    }

    @Logging("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResponseEntity<ResponseData> updateDept(@RequestBody DeptDTO dept) {
        deptService.updateDept(dept);
        return success();
    }

    @Logging("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResponseEntity<ResponseData> deleteDept(@RequestBody Set<Long> deptIds) {
        deptService.deleteByDeptIds(deptIds);
        return success();
    }
}
