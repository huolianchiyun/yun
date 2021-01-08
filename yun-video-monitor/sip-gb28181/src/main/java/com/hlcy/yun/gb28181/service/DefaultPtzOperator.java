package com.hlcy.yun.gb28181.service;

import com.hlcy.yun.gb28181.bean.api.PtzParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPtzOperator implements PtzOperator {
    private final GB28181Properties properties;

    @Override
    public void control(PtzParams ptzParams) {



    }
}
