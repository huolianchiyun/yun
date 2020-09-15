package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.DictQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

@Api(tags = "系统：字典管理")
@RestController
@RequestMapping("/yun/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @Logging("导出字典数据")
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(@RequestParam DictQueryCriteria criteria, HttpServletResponse response) throws IOException {
        dictService.download(dictService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询")
    @ApiOperation("根据ID查询")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<ResponseData> query(@PathVariable Long id) {
        return success(dictService.queryById(id));
    }

    @Logging("查询字典类型列表")
    @ApiOperation("查询字典类型列表")
    @GetMapping("/type")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<ResponseData> queryDictType() {
        return success(dictService.queryDictType());
    }

    @Logging("查询字典类型列表")
    @ApiOperation("查询字典类型列表")
    @GetMapping("/type/{dictType}")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<ResponseData> queryByDictType(@PathVariable String dictType) {
        return success(dictService.queryByDictType(dictType));
    }

    @Logging("根据条件查询分页")
    @ApiOperation("根据条件查询分页")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<ResponseData> queryByCriteria(DictQueryCriteria criteria) {
        return success(dictService.queryAllByCriteria(criteria));
    }

    @Logging("新增字典")
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<ResponseData> create(@Validated @RequestBody DictDO dict) {
        dictService.create(dict);
        return success();
    }

    @Logging("修改字典")
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<ResponseData> update(@RequestBody DictDO dict) {
        dictService.update(dict);
        return success();
    }

    @Logging("删除字典")
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody Long id) {
        dictService.deleteById(id);
        return success();
    }
}
