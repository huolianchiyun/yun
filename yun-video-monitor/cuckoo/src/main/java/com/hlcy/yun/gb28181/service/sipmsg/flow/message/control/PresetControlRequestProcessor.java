package com.hlcy.yun.gb28181.service.sipmsg.flow.message.control;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

/**
 * 预置位请求处理器
 */
@Slf4j
public class PresetControlRequestProcessor extends MessageRequestProcessor {

    @Override
    public void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <PTZPreset> request, message: \n{}", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        String result = XmlUtil.getTextOfChildTagFrom(rootElement, "Result");
        if ("OK".equals(result)) {
            DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_PRESET + deviceId, "OK");
        } else {
            DeferredResultHolder.setErrorDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_PRESET + deviceId, "ERROR");
        }
    }
}
