package com.hlcy.yun.gb28181.api;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "GB28181：设备通知 API")
@RequiredArgsConstructor
@RequestMapping("/yun/notify")
@RestController
public class NotifyController {


}
