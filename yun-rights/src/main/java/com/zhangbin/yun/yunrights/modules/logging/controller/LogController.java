package com.zhangbin.yun.yunrights.modules.logging.controller;

import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import static com.zhangbin.yun.yunrights.modules.logging.enums.LogLevel.*;
import com.zhangbin.yun.yunrights.modules.logging.model.criteria.LogQueryCriteria;
import com.zhangbin.yun.yunrights.modules.logging.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Api(tags = "系统：日志管理")
public class LogController {

    private final LogService logService;

    @Logging("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        logService.download(criteria.setLogLevel(INFO), response);
    }

    @Logging("导出错误数据")
    @ApiOperation("导出错误数据")
    @GetMapping(value = "/download/error")
    @PreAuthorize("@el.check()")
    public void downloadErrorLog(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        logService.download(criteria.setLogLevel(ERROR), response);
    }

    @GetMapping
    @ApiOperation("日志查询")
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> query(LogQueryCriteria criteria) {
        return success(logService.queryAllByCriteria(criteria.setLogLevel(INFO)));
    }

    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity<ResponseData> queryUserLog(LogQueryCriteria criteria) {
        return success(logService.queryAllByCriteria(criteria.setBlurry(SecurityUtils.getCurrentUsername()).setLogLevel(INFO)));
    }

    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> queryErrorLog(LogQueryCriteria criteria) {
        return success(logService.queryAllByCriteria(criteria.setLogLevel(ERROR)));
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> queryErrorLogs(@PathVariable Long id) {
        return success(logService.queryExceptionalDetailById(id));
    }

    @DeleteMapping(value = "/del/error")
    @Logging("删除所有ERROR日志")
    @ApiOperation("删除所有ERROR日志")
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> delAllErrorLog() {
        logService.deleteAllLogsByErrorLevel();
        return success();
    }

    @DeleteMapping(value = "/del/info")
    @Logging("删除所有INFO日志")
    @ApiOperation("删除所有INFO日志")
    @PreAuthorize("@el.check()")
    public ResponseEntity<ResponseData> delAllInfoLog() {
        logService.delAllLogsByInfoLevel();
        return success();
    }
}
