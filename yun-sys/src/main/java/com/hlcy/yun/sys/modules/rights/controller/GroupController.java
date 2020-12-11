package com.hlcy.yun.sys.modules.rights.controller;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.GroupQueryCriteria;
import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import com.hlcy.yun.sys.modules.rights.service.GroupService;
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
@Api(tags = "系统：组管理")
@RequestMapping("/yun/group")
public class GroupController {

    private final GroupService groupService;

    @Logging("导出组数据")
    @ApiOperation("导出组数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('group:list')")
    public void download(HttpServletResponse response, GroupQueryCriteria criteria) throws Exception {
        criteria.setPid(null);
        groupService.downloadExcel(groupService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询组")
    @ApiOperation("根据ID查询组")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('user:list','group:list')")
    public ResponseEntity<ResponseData<GroupDO>> query(@PathVariable Long id) {
        return success(groupService.queryById(id));
    }

    @ApiOperation("根据父组ID查询子组")
    @GetMapping(value = "/lazy/{pid}")
    @PreAuthorize("@el.check('group:list')")
    public ResponseEntity<ResponseData<List<GroupDO>>> queryByPid(@PathVariable Long pid) {
        return success(groupService.queryByPid(pid));
    }

    @Logging("根据条件查询组")
    @ApiOperation("根据条件查询组：1、根据 pid 查询满足条件的子组（直接子组）2、将 pid 设置为 null可以查询所有满足条件的组")
    @GetMapping
    @PreAuthorize("@el.check('user:list','group:list')")
    public ResponseEntity<ResponseData<List<GroupDO>>> query(GroupQueryCriteria criteria) {
        return success(groupService.queryAllByCriteriaWithNoPage(criteria));
    }

    @Logging("查询组:根据ID获取同级与上级数据")
    @ApiOperation("查询组:根据ID获取同级与上级数据")
    @PostMapping("/tree2me")
    @PreAuthorize("@el.check('user:list','group:list')")
    public ResponseEntity<ResponseData<List<GroupDO>>> queryAncestorAndSibling(@RequestBody Set<Long> groupIds) {
        return success(groupService.queryAncestorAndSibling(groupIds));
    }

    @Logging("新增组")
    @ApiOperation("新增组")
    @PostMapping
    @PreAuthorize("@el.check('group:add')")
    public ResponseEntity<ResponseData<Void>> create(@Validated(GroupDO.Create.class) @RequestBody GroupDO group) {
        groupService.create(group);
        return success();
    }

    @Logging("修改组")
    @ApiOperation("修改组")
    @PutMapping
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<ResponseData<Void>> update(@Validated(GroupDO.Update.class) @RequestBody GroupDO group) {
        groupService.update(group);
        return success();
    }

    @Logging("删除组")
    @ApiOperation("删除组")
    @DeleteMapping
    @PreAuthorize("@el.check('group:del')")
    public ResponseEntity<ResponseData<Void>> deleteByIds(@RequestBody Set<Long> ids) {
        groupService.deleteByIds(ids);
        return success();
    }
}
