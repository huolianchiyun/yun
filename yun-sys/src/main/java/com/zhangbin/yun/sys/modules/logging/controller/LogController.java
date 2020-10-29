package com.zhangbin.yun.sys.modules.logging.controller;

import static com.zhangbin.yun.common.web.response.ResponseUtil.success;

import com.zhangbin.yun.common.page.PageInfo;
import com.zhangbin.yun.common.web.response.ResponseData;
import com.zhangbin.yun.common.spring.security.SecurityUtils;
import com.zhangbin.yun.sys.modules.logging.annotation.Logging;
import static com.zhangbin.yun.sys.modules.logging.enums.LogLevel.*;

import com.zhangbin.yun.sys.modules.logging.model.$do.LogDO;
import com.zhangbin.yun.sys.modules.logging.model.criteria.LogQueryCriteria;
import com.zhangbin.yun.sys.modules.logging.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yun/logs")
@Api(tags = "系统：日志管理")
public class LogController {

    private final LogService logService;

    @Logging("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('all')")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        logService.download(criteria.setLogLevel(INFO), response);
    }

    @Logging("导出错误数据")
    @ApiOperation("导出错误数据")
    @GetMapping(value = "/download/error")
    @PreAuthorize("@el.check('all')")
    public void downloadErrorLog(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        logService.download(criteria.setLogLevel(ERROR), response);
    }

    @GetMapping
    @ApiOperation("根据条件分页查询日志")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<PageInfo<List<LogDO>>>> query(LogQueryCriteria criteria) {
        return success(logService.queryByCriteria(criteria.setLogLevel(INFO)));
    }

    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity<ResponseData<PageInfo<List<LogDO>>>> queryUserLog(LogQueryCriteria criteria) {
        return success(logService.queryByCriteria(criteria.setBlurry(SecurityUtils.getCurrentUsername()).setLogLevel(INFO)));
    }

    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<PageInfo<List<LogDO>>>> queryErrorLog(LogQueryCriteria criteria) {
        return success(logService.queryByCriteria(criteria.setLogLevel(ERROR)));
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Object>> queryErrorLogs(@PathVariable Long id) {
        return success(logService.queryExceptionalDetailById(id));
    }

    @DeleteMapping(value = "/del/error")
    @Logging("删除所有ERROR日志")
    @ApiOperation("删除所有ERROR日志")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Void>> delAllErrorLog() {
        logService.deleteAllLogsByErrorLevel();
        return success();
    }

    @DeleteMapping(value = "/del/info")
    @Logging("删除所有INFO日志")
    @ApiOperation("删除所有INFO日志")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Void>> delAllInfoLog() {
        logService.delAllLogsByInfoLevel();
        return success();
    }
}
