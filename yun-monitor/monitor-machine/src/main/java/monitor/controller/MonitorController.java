package monitor.controller;

import com.zhangbin.yun.common.web.response.ResponseData;
import monitor.model.vo.MachineInfo;
import monitor.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.zhangbin.yun.common.web.response.ResponseUtil.success;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统: 服务监控管理")
@RequestMapping("/yun/monitor")
public class MonitorController {

    private final MonitorService monitorService;

    @GetMapping
    @ApiOperation("查询服务机器监控信息")
    @PreAuthorize("@el.check('monitor')")
    public ResponseEntity<ResponseData<MachineInfo>> query() {
        return success(monitorService.getLocalMachineInfo());
    }
}
