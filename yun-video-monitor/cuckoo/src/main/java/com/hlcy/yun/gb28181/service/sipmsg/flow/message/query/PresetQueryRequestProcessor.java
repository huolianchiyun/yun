package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

/**
 * 设备配置查询请求处理器
 */
@Slf4j
public class PresetQueryRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <PresetQuery> request, message: {}.", event.getRequest());
        }
    }
}
