package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.gb28181.service.PtzOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/yun/ptz")
@RestController
public class PtzController {


    private final PtzOperator operator;
}
