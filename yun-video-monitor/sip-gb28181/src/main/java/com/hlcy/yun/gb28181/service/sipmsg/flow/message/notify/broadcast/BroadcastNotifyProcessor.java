package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast;

import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

import static com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder.setErrorDeferredResultForRequest;
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
        if (!"OK".equalsIgnoreCase(result)) {
            setErrorDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_VOICE + deviceId, "设备广播异常， 响应结果：ERROR");
            cleanContextCache(deviceId);
            return;
        }
        final FlowContext flowContext = FlowContextCacheUtil.get(deviceId);
        flowContext.setCurrentRequestProcessor2next();
    }

    private void cleanContextCache(String deviceId) {
        final FlowContext flowContext = FlowContextCacheUtil.get(deviceId);
        if(flowContext != null){
            flowContext.clearSessionCache();
            FlowContextCacheUtil.remove(deviceId);
        }
    }
}
