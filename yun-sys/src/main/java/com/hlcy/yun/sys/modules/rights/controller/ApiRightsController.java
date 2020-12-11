package com.hlcy.yun.sys.modules.rights.controller;

import com.hlcy.yun.sys.modules.rights.model.$do.ApiRightsDO;
import com.hlcy.yun.sys.modules.rights.model.common.NameValue;
import com.hlcy.yun.sys.modules.rights.service.ApiRightsService;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.sys.modules.logging.annotation.Logging;
import com.hlcy.yun.sys.modules.rights.model.criteria.ApiRightsQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

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
    public ResponseEntity<ResponseData<PageInfo<List<ApiRightsDO>>>> queryByCriteria(@Validated ApiRightsQueryCriteria criteria) {
        return success(apiRightsService.queryByCriteria(criteria));
    }

    @Logging("查询 API 访问权限")
    @ApiOperation("查询 API 访问权限")
    @GetMapping("/rights/group")
    @PreAuthorize("@el.check('all')")
    public ResponseEntity<ResponseData<List<NameValue>>> queryApiGroup() {
        return success(apiRightsService.queryAipGroup());
    }
}
