package com.zhangbin.yun.yunrights.modules.rights.controller;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.common.utils.PageUtil;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.GroupQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
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
        groupService.download(groupService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("查询组")
    @ApiOperation("查询组")
    @GetMapping
    @PreAuthorize("@el.check('user:list','group:list')")
    public ResponseEntity<ResponseData> query(GroupQueryCriteria criteria) throws Exception {
        List<GroupDO> groups = groupService.queryAllByCriteriaWithNoPage(criteria);
        return success(PageUtil.toPage(groups, groups.size()));
    }

    @Logging("查询组")
    @ApiOperation("查询组:根据ID获取同级与上级数据")
    @PostMapping("/batch/family")
    @PreAuthorize("@el.check('user:list','group:list')")
    public ResponseEntity<ResponseData> getSuperior(@RequestBody Set<Long> groupIds) {
        return success(groupService.queryAncestorAndSiblingOfDepts(groupIds));
    }

    @Logging("新增组")
    @ApiOperation("新增组")
    @PostMapping
    @PreAuthorize("@el.check('group:add')")
    public ResponseEntity<ResponseData> createGroup(@Validated @RequestBody GroupDO group) {
        groupService.createGroup(group);
        return success();
    }

    @Logging("修改组")
    @ApiOperation("修改组")
    @PutMapping
    @PreAuthorize("@el.check('group:edit')")
    public ResponseEntity<ResponseData> updateGroup(@RequestBody GroupDO group) {
        groupService.updateGroup(group);
        return success();
    }

    @Logging("删除组")
    @ApiOperation("删除组")
    @DeleteMapping
    @PreAuthorize("@el.check('group:del')")
    public ResponseEntity<ResponseData> deleteByGroupIds(@RequestBody Set<Long> groupIds) {
        groupService.deleteByGroupIds(groupIds);
        return success();
    }
}
