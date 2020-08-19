package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.common.utils.PageUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.MenuQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

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
@RequestMapping("/yun/menu")
public class MenuController {

    private final MenuService menuService;

    @Logging("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
        menuService.download(menuService.queryAllByCriteriaWithNoPage(criteria.setPid(null)), response);
    }

    @GetMapping(value = "/user")
    @ApiOperation("获取当前用户菜单")
    public ResponseEntity<ResponseData> getMenusForUser() {
        return success(menuService.queryByUser(SecurityUtils.getCurrentUserId()));
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<ResponseData> query(@RequestParam Long pid) {
        return success(menuService.querySubmenusByPid(pid));
    }

    @Logging("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<ResponseData> query(MenuQueryCriteria criteria) {
        List<MenuDO> menuDtoList = menuService.queryAllByCriteriaWithNoPage(criteria);
        return success(PageUtil.toPage(menuDtoList, menuDtoList.size()));
    }

    @Logging("查询菜单")
    @ApiOperation("查询菜单: 获取多个菜单作为叶子节点的菜单树，若 menuIds 为空，则获取所有根级菜单")
    @PostMapping("/tree2me")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<ResponseData> queryAncestorAndSiblingOfMenus(@RequestBody List<Long> menuIds) {
        return success(menuService.queryAncestorAndSiblingOfMenus(menuIds));
    }

    @Logging("新增菜单")
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<ResponseData> createMenu(@RequestBody MenuDO menu) {
        menuService.createMenu(menu);
        return success();
    }

    @Logging("修改菜单")
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<ResponseData> updateMenu(@RequestBody MenuDO menu) {
        menuService.updateMenu(menu);
        return success();
    }

    @Logging("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<ResponseData> deleteMenus(@RequestBody Set<Long> menuIds) {
        menuService.deleteByMenuIds(menuIds);
        return success();
    }
}
