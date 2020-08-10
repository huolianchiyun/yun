package com.zhangbin.yun.yunrights.modules.rights.controller;

import cn.hutool.core.lang.Dict;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.RoleQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：角色管理")
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<ResponseData> query(@PathVariable Long id) {
        return success(roleService.queryById(id));
    }

    @ApiOperation("不分页查询满足条件的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<ResponseData> query() {
        return success(roleService.queryAllByCriteriaWithNoPage(null));
    }

    @Logging("分页查询满足条件的角色")
    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<ResponseData> query(RoleQueryCriteria criteria) {
        return success(roleService.queryAllByCriteria(criteria));
    }

    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public ResponseEntity<ResponseData> getLevel() {
        return success(Dict.create().set("level", roleService.getLevelOfCurrentUserMaxRole()));
    }

    @Logging("新增角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<ResponseData> create(@Validated @RequestBody RoleDO role) {
        roleService.createRole(role);
        return success();
    }

    @Logging("修改角色")
    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<ResponseData> update(@RequestBody RoleDO role) {
        roleService.updateRole(role);
        return success();
    }

    @Logging("修改角色关联的菜单")
    @ApiOperation("修改角色关联的菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<ResponseData> updateMenu(@RequestBody RoleDO role) {
        roleService.updateAssociatedMenu(role);
        return success();
    }

    @Logging("删除角色")
    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<ResponseData> delete(@RequestBody Set<Long> ids) {
        roleService.batchDeleteRoles(ids);
        return success();
    }

    @Logging("导出角色数据")
    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAllByCriteriaWithNoPage(criteria), response);
    }

}
