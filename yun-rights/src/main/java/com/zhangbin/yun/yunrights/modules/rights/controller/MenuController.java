package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.common.response.ResponseData;
import com.zhangbin.yun.yunrights.common.utils.PageUtil;
import com.zhangbin.yun.yunrights.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.model.MenuQueryConditions;
import com.zhangbin.yun.yunrights.modules.rights.model.entity.Menu;
import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;
import static com.zhangbin.yun.yunrights.common.response.ResponseUtil.success;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@Api(tags = "系统：菜单管理")
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private final static String ENTITY_NAME = "menu";
    private final MenuService menuService;

    //@Log("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryConditions queryConditions) throws Exception {
        menuService.download(menuService.queryAll(queryConditions, false), response);
    }

    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<ResponseData> buildMenus() {
        return success(menuService.buildTree(menuService.findByUser(SecurityUtils.getCurrentUserId())));
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<ResponseData> query(@RequestParam Long pid) {
        return success(menuService.getMenus(pid));
    }

    //    @Log("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<ResponseData> query(MenuQueryConditions criteria) throws Exception {
        List<Menu> menuDtoList = menuService.queryAll(criteria, true);
        return success(PageUtil.toPage(menuDtoList, menuDtoList.size()));
    }

    //    @Log("查询菜单")
    @ApiOperation("查询菜单:根据多个菜单ID，获取其同级与上级数据")
    @PostMapping("/batch/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<ResponseData> queryFatherAndSiblingForMultiMenus(@RequestBody List<Long> menuIds) {
        return success(menuService.queryFatherAndSiblingForMultiMenus(menuIds));
    }

    //    @Log("新增菜单")
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<ResponseData> create(@RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot have an ID.");
        }
        menuService.create(resources);
        return success();
    }

    //    @Log("修改菜单")
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<ResponseData> update(@RequestBody Menu resources) {
        menuService.update(resources);
        return success();
    }

    //    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<ResponseData> delete(@RequestBody Set<Long> menuIds) {
        menuService.delete(menuIds);
        return success();
    }
}
