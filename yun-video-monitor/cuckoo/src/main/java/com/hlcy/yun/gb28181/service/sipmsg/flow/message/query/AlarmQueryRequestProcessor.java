package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.bean.DeviceInfo;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

/**
 * 报警查询请求处理器
 */
@Slf4j
public class AlarmQueryRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {// TODO 未开发
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Alarm> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        final DeviceInfo device = new DeviceInfo().setDeviceId(deviceId);

        DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_Query_ALARM + deviceId, device);
    }


}
