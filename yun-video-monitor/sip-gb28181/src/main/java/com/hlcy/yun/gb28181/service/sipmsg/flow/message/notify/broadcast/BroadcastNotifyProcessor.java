package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

import static com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder.setErrorDeferredResultForRequest;
import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.BROADCAST;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;


@Slf4j
public class BroadcastNotifyProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Broadcast> request, message: \n{}", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
        String result = getTextOfChildTagFrom(rootElement, "Result");
        if ("ERROR".equalsIgnoreCase(result)) {
            setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_VOICE + deviceId,
                    String.format("设备广播异常， 原因：%s",  getTextOfChildTagFrom(rootElement.element("Info"), "Reason")));
            cleanContextCache(deviceId);
        }
    }

    private void cleanContextCache(String deviceId) {
        final FlowContext flowContext = FlowContextCacheUtil.get(BROADCAST + deviceId);
        if (flowContext != null) {
            flowContext.clearSessionCache();
            FlowContextCacheUtil.remove(deviceId);
        }
    }
}
