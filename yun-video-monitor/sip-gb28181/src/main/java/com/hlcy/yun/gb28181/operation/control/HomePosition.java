package com.hlcy.yun.gb28181.operation.control;


import com.hlcy.yun.gb28181.bean.api.HomePositionParams;
import com.hlcy.yun.gb28181.config.GB28181Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomePosition implements ControlCmd<HomePositionParams> {
    private final GB28181Properties properties;

    @Override
    public void execute(HomePositionParams homePositionParams) {

    }
}
