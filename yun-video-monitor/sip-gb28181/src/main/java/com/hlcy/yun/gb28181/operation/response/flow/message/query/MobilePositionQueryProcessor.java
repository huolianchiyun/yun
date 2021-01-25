package com.hlcy.yun.gb28181.operation.response.flow.message.query;

import com.hlcy.yun.gb28181.operation.response.flow.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;

/**
 * 移动设备位置数据查询
 */
@Slf4j
public class MobilePositionQueryProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MobilePosition> request, message: {}.", event.getRequest());
        }
    }
}
