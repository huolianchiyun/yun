package com.hlcy.yun.sys.modules.logging.controller;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;

import com.hlcy.yun.sys.modules.logging.enums.LogLevel;
import com.hlcy.yun.sys.modules.logging.model.$do.LogDO;
import com.hlcy.yun.sys.modules.logging.model.criteria.LogQueryCriteria;
import com.hlcy.yun.sys.modules.logging.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/yun/log")
@Api(tags = "系统：日志管理")
public class LogController {

    private final LogService logService;

    @Logging("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('all')")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) {
        logService.download(criteria, response);
    }

    @GetMapping
    @ApiOperation("根据条件分页查询日志")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<PageInfo<List<LogDO>>>> query(LogQueryCriteria criteria) {
        return success(logService.queryByCriteria(criteria));
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Object>> queryErrorLogs(@PathVariable Long id) {
        return success(logService.queryExceptionalDetailById(id));
    }

    @DeleteMapping(value = "/del/{logLevel}")
    @Logging("根据日志等级删除日志")
    @ApiOperation("根据日志等级删除日志")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<Void>> delLogByLevel(@PathVariable LogLevel logLevel) {
        logService.delLogsByLevel(logLevel);
        return success();
    }
}
