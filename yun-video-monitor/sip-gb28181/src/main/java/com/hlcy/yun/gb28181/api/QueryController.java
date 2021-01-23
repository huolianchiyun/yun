package com.hlcy.yun.gb28181.api;


import com.hlcy.yun.gb28181.config.GB28181Properties;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "GB28181：设备查询 API")
@RequiredArgsConstructor
@RequestMapping("/yun/query")
@RestController
public class QueryController {
    private final GB28181Properties properties;

}
