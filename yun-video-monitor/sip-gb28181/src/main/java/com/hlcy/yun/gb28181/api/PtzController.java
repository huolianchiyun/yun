package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.common.web.response.ResponseUtil;
import com.hlcy.yun.gb28181.bean.api.PtzParams;
import com.hlcy.yun.gb28181.service.PtzOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hlcy.yun.common.web.response.ResponseUtil.success;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/yun/ptz")
@RestController
public class PtzController {

    private final PtzOperator operator;

    @PostMapping("/control")
    public ResponseEntity<ResponseData<Void>> ptzControl(@RequestBody PtzParams ptzParams) {
        operator.control(ptzParams);
        return success();
    }
}
