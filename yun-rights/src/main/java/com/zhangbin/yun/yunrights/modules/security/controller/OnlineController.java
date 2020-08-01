package com.zhangbin.yun.yunrights.modules.security.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.common.utils.EncryptUtils;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> query(String filter, Pageable pageable) {
        return success(onlineUserService.getAll(filter, pageable));
    }

    @Logging("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
        return success();
    }
}
