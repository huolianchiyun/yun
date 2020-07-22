//package com.zhangbin.yun.yunrights.modules.rights.api;
//
//import com.zhangbin.yun.yunrights.modules.rights.mapper.MenuMapper;
//import com.zhangbin.yun.yunrights.modules.rights.model.Menu;
//import com.zhangbin.yun.yunrights.modules.rights.service.MenuService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.CollectionUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import javax.servlet.http.HttpServletResponse;
//import java.util.*;
//
//
//@RestController
//@Api(tags = "系统：菜单管理")
//@RequiredArgsConstructor
//@RequestMapping("/api/menus")
//public class MenuController {
//
//    private final MenuService menuService;
//    private final MenuMapper menuMapper;
//    private static final String ENTITY_NAME = "menu";
//
////    @Log("导出菜单数据")
//    @ApiOperation("导出菜单数据")
//    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('menu:list')")
//    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws Exception {
//        menuService.download(menuService.queryAll(criteria, false), response);
//    }
//
//    @GetMapping(value = "/build")
//    @ApiOperation("获取前端所需菜单")
//    public ResponseEntity<Object> buildMenus() {
//        List<Menu> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
//        List<Menu> menuDtos = menuService.buildTree(menuDtoList);
//        return new ResponseEntity<>(menuService.buildMenus(menuDtos), HttpStatus.OK);
//    }
//
//    @ApiOperation("返回全部的菜单")
//    @GetMapping(value = "/lazy")
//    @PreAuthorize("@el.check('menu:list','roles:list')")
//    public ResponseEntity<Object> query(@RequestParam Long pid) {
//        return new ResponseEntity<>(menuService.getMenus(pid), HttpStatus.OK);
//    }
//
////    @Log("查询菜单")
//    @ApiOperation("查询菜单")
//    @GetMapping
//    @PreAuthorize("@el.check('menu:list')")
//    public ResponseEntity<Object> query(MenuQueryCriteria criteria) throws Exception {
//        List<Menu> menuDtoList = menuService.queryAll(criteria, true);
//        return new ResponseEntity<>(PageUtil.toPage(menuDtoList, menuDtoList.size()), HttpStatus.OK);
//    }
//
////    @Log("查询菜单")
//    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
//    @PostMapping("/superior")
//    @PreAuthorize("@el.check('menu:list')")
//    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
//        Set<Menu> menuDtos = new LinkedHashSet<>();
//        if (!CollectionUtils.isEmpty(ids)) {
//            for (Long id : ids) {
//                Menu menu = menuService.findById(id);
//                menuDtos.addAll(menuService.getSuperior(menu, new ArrayList<>()));
//            }
//            return new ResponseEntity<>(menuService.buildTree(new ArrayList<>(menuDtos)), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(menuService.getMenus(null), HttpStatus.OK);
//    }
//
////    @Log("新增菜单")
//    @ApiOperation("新增菜单")
//    @PostMapping
//    @PreAuthorize("@el.check('menu:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody Menu resources) {
//        if (resources.getId() != null) {
//            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
//        }
//        menuService.create(resources);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
////    @Log("修改菜单")
//    @ApiOperation("修改菜单")
//    @PutMapping
//    @PreAuthorize("@el.check('menu:edit')")
//    public ResponseEntity<Object> update(@Validated(Menu.Update.class) @RequestBody Menu resources) {
//        menuService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
////    @Log("删除菜单")
//    @ApiOperation("删除菜单")
//    @DeleteMapping
//    @PreAuthorize("@el.check('menu:del')")
//    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
//        Set<Menu> menuSet = new HashSet<>();
//        for (Long id : ids) {
//            List<Menu> menuList = menuService.getMenus(id);
//            menuSet.add(menuService.findOne(id));
//            menuSet = menuService.getDeleteMenus(menuMapper.toEntity(menuList), menuSet);
//        }
//        menuService.delete(menuSet);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}
