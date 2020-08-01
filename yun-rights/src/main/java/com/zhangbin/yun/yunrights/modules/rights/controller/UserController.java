package com.zhangbin.yun.yunrights.modules.rights.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

import com.zhangbin.yun.yunrights.modules.common.utils.PageUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.RsaUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.UserDto;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.rights.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    //    private final DataService dataService;
//    private final DeptService deptService;
    private final RoleService roleService;
    private final VerifyService verificationCodeService;

    @Logging("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void download(UserQueryCriteria criteria, HttpServletResponse response,) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @Logging("查询用户")
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<ResponseData> queryByCriteria(UserQueryCriteria criteria, Pageable pageable) {
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            criteria.getDeptIds().addAll(deptService.getDeptChildren(criteria.getDeptId(),
                    deptService.findByPid(criteria.getDeptId())));
        }

        // 数据权限
        List<Long> dataScopes = dataService.getDeptIds(userService.findByName(SecurityUtils.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(criteria.getDeptIds())) {
                return success(userService.queryAll(criteria, pageable));
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return success(userService.queryAll(criteria));
        }
        return success(PageUtil.toPage(null, 0));
    }

    @Logging("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<ResponseData> createUser(@Validated @RequestBody UserDo user) {
        checkLevel(user);
        userService.createUser(user);
        return success();
    }

    @Logging("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<ResponseData> updateUser(@RequestBody UserDo user) {
        checkLevel(user);
        userService.updateUser(user);
        return success();
    }

    @Logging("修改用户：个人中心")
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<ResponseData> updateCenter(@RequestBody UserDo resources) {
        if (!resources.getId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return success();
    }

    @Logging("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<ResponseData> deleteUsers(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.findById(id).getUsername());
            }
        }
        userService.deleteUsers(ids);
        return success();
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<ResponseData> updatePwd(@RequestBody UserPassVo passVo) {

        return success();
    }

    @Logging("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<ResponseData> updateEmail(@PathVariable String code, @RequestBody UserDo user) throws Exception {

        return success();
    }


//    @ApiOperation("修改头像")
//    @PostMapping(value = "/updateAvatar")
//    public ResponseEntity<ResponseData> updateAvatar(@RequestParam MultipartFile avatar) {
//        return success(userService.updateAvatar(avatar));
//    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param user /
     */
    private void checkLevel(UserDo user) {
        Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId())
                .stream()
                .map(RoleSmallDto::getLevel)
                .collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(user.g);
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
