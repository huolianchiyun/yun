package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

/**
 * 移动设备位置数据查询
 */
@Slf4j
public class MobilePositionQueryRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MobilePosition> request, message: {}.", event.getRequest());
        }
    }
}
