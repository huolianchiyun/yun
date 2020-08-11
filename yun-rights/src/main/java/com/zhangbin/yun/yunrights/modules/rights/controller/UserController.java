package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
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
    public void download(UserQueryCriteria criteria, HttpServletResponse response) throws IOException {
        userService.download(userService.queryAllByCriteriaWithNoPage(criteria), response);
    }

    @Logging("查询用户")
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<ResponseData> queryByCriteria(UserQueryCriteria criteria) {
        return success(userService.queryAllByCriteria(criteria));
    }

    @Logging("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<ResponseData> createUser(@Validated @RequestBody UserDO user) {
        userService.createUser(user);
        return success();
    }

    @Logging("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<ResponseData> updateUser(@RequestBody UserDO user) {
        userService.updateUser(user);
        return success();
    }

    @Logging("修改用户：个人中心")
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<ResponseData> updateCenter(@RequestBody UserDO user) {
        userService.updateUser(user);
        return success();
    }

    @Logging("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<ResponseData> deleteUsers(@RequestBody Set<Long> ids) {
        userService.deleteByUserIds(ids);
        return success();
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<ResponseData> updatePwd(@RequestBody UserPwdVO pwdVo) throws Exception {
        userService.updatePwd(pwdVo);
        return success();
    }

    @Logging("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<ResponseData> updateEmail(@PathVariable String code, @RequestBody UserDO user) throws Exception {
        userService.updateEmail(code, user);
        return success();
    }
}
