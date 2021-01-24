package com.hlcy.yun.gb28181.operation.response.flow.query;

import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

/**
 * 设备配置查询请求处理器
 */
@Slf4j
public class PresetQueryProcessor extends MessageQueryProcessor {

    @Override
    protected void process(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <PresetQuery> request, message: {}.", event.getRequest());
        }
    }
}
