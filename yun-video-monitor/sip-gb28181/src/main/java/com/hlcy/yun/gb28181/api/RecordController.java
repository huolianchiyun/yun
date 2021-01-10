package com.hlcy.yun.gb28181.api;

import com.hlcy.yun.common.web.response.ResponseData;
import com.hlcy.yun.gb28181.bean.api.PtzParams;
import com.hlcy.yun.gb28181.bean.api.RecordParams;
import com.hlcy.yun.gb28181.service.PtzOperator;
import com.hlcy.yun.gb28181.service.RecordOperator;
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
@RequestMapping("/yun/record")
@RestController
public class RecordController {

    private final RecordOperator operator;

    @PostMapping("/control")
    public ResponseEntity<ResponseData<Void>> recordControl(@RequestBody RecordParams recordParams) {
        operator.operate(recordParams);
        return success();
    }
}
