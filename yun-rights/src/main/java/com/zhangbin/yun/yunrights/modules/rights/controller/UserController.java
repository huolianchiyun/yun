package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.datarights.RuleManager;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.DictDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/yun/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Logging("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(@RequestParam UserQueryCriteria criteria, HttpServletResponse response) throws IOException {
        userService.download(userService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询用户")
    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<ResponseData> query(@PathVariable Long id) {
        return success(userService.queryById(id));
    }

    @Logging("根据条件查询用户")
    @ApiOperation("根据条件查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    // 不加注解，默认从url拿数据封装成controller参数对象，加 @RequestBody 注解 spring mvc才会从http body里去拿数据。
    public ResponseEntity<ResponseData> queryByCriteria(@Validated UserQueryCriteria criteria) {
       return success(userService.queryAllByCriteria(criteria));
    }

    @Logging("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<ResponseData> create(@Validated(UserDO.Create.class) @RequestBody UserDO user) {
        userService.create(user);
        return success();
    }

    @Logging("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<ResponseData> update(@Validated(UserDO.Update.class) @RequestBody UserDO user) {
        userService.update(user);
        return success();
    }

    @Logging("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody Set<Long> ids) {
        userService.deleteByIds(ids);
        return success();
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/update/pwd")
    public ResponseEntity<ResponseData> updatePwd(@RequestBody UserPwdVO pwdVo) throws Exception {
        userService.updatePwd(pwdVo);
        return success();
    }

    @Logging("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/update/email/{code}")
    public ResponseEntity<ResponseData> updateEmail(@PathVariable String code, @RequestBody UserDO user) throws Exception {
        userService.updateEmail(code, user);
        return success();
    }
}
