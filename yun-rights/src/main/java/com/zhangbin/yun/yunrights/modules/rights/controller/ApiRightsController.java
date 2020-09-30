package com.zhangbin.yun.yunrights.modules.rights.controller;

import com.zhangbin.yun.yunrights.modules.common.response.ResponseData;
import com.zhangbin.yun.yunrights.modules.logging.annotation.Logging;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.ApiRightsQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.service.ApiRightsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.zhangbin.yun.yunrights.modules.common.response.ResponseUtil.success;

@Api(tags = "系统：API 访问权限")
@RestController
@RequestMapping("/yun/api")
@RequiredArgsConstructor
public class ApiRightsController {

    private final ApiRightsService apiRightsService;

    @Logging("查询 API 访问权限")
    @ApiOperation("查询 API 访问权限")
    @GetMapping("/rights")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData> queryByCriteria(@Validated ApiRightsQueryCriteria criteria) {
        return success(apiRightsService.queryAllByCriteria(criteria));
    }

    @Logging("查询 API 访问权限")
    @ApiOperation("查询 API 访问权限")
    @GetMapping("/rights/group")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData> queryApiGroup() {
        return success(apiRightsService.queryAipGroup());
    }
}
