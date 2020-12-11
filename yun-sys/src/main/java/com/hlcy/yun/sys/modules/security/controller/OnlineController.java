package com.hlcy.yun.sys.modules.security.controller;

import com.hlcy.yun.sys.modules.security.model.dto.OnlineUser;
import com.hlcy.yun.sys.modules.security.service.OnlineUserService;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.common.utils.encodec.EncryptUtils;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yun/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData<PageInfo<List<OnlineUser>>>> query(String filter, PageInfo<List<OnlineUser>> pageInfo) {
        return success(onlineUserService.getAllOnlineUsersWithPage(filter, pageInfo));
    }

    @Logging("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAllOnlineUsersWithNoPage(filter), response);
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData<Void>> delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
        return success();
    }
}
