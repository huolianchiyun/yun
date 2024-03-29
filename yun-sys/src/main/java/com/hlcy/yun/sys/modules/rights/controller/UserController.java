package com.hlcy.yun.sys.modules.rights.controller;

import com.hlcy.yun.common.web.response.Meta;
import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousPostMapping;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.UserQueryCriteria;
import com.hlcy.yun.sys.modules.rights.service.UserService;
import com.hlcy.yun.sys.modules.common.annotation.rest.AnonymousGetMapping;
import com.hlcy.yun.common.model.BaseDO;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.web.response.ResponseData;
import static com.hlcy.yun.common.web.response.ResponseUtil.success;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import com.hlcy.yun.sys.modules.rights.model.vo.UserEmailVO;
import com.hlcy.yun.sys.modules.rights.model.vo.UserPwdVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/yun/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Logging("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(UserQueryCriteria criteria, HttpServletResponse response) {
        userService.downloadExcel(userService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("根据ID查询用户")
    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<ResponseData<UserDO>> query(@PathVariable Long id) {
        return success(userService.queryById(id));
    }

    @Logging("根据条件查询用户")
    @ApiOperation("根据条件查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    // 不加注解，默认从url拿数据封装成controller参数对象，加 @RequestBody 注解 spring mvc才会从http body里去拿数据。
    public ResponseEntity<ResponseData<PageInfo<List<UserDO>>>> queryByCriteria(@Validated UserQueryCriteria criteria) {
       return success(userService.queryByCriteria(criteria));
    }

    @ApiOperation("根据用户名验证密码是否过期")
    @GetMapping("/pwd/expired")
    @AnonymousGetMapping("/pwd/expired")
    public ResponseEntity<ResponseData<Meta>> verifyPasswordExpired(@NotBlank(message = "username不能为空") @Param("username") String username) {
        return success(userService.verifyPasswordExpired(username));
    }

    @Logging("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<ResponseData<Void>> create(@Validated(UserDO.Create.class) @RequestBody UserDO user) {
        userService.create(user);
        return success();
    }

    @Logging("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<ResponseData<Void>> update(@Validated(UserDO.Update.class) @RequestBody UserDO user) {
        userService.update(user);
        return success();
    }

    @Logging("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<ResponseData<Void>> deleteByIds(@RequestBody Set<Long> ids) {
        userService.deleteByIds(ids);
        return success();
    }

    @ApiOperation("修改密码")
    @AnonymousPostMapping("/update/pwd")
    public ResponseEntity<ResponseData<Void>> updatePwd(@Validated @RequestBody UserPwdVO pwdVo) throws Exception {
        userService.updatePwd(pwdVo);
        return success();
    }
    @ApiOperation("重置密码")
    @PutMapping("/reset/pwd/{username}")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Void>> resetPwd(@PathVariable("username") String username) {
        userService.resetPwd(username);
        return success();
    }

    @Logging("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/update/email")
    public ResponseEntity<ResponseData<Void>> updateEmail(@Validated(BaseDO.Update.class) @RequestBody UserEmailVO userEmail) throws Exception {
        userService.updateEmail(userEmail);
        return success();
    }
}
