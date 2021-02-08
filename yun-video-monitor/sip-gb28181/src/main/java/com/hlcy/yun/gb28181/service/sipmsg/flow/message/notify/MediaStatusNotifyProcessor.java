package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import javax.sip.RequestEvent;

@Slf4j
public class MediaStatusNotifyProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MediaStatus> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");

        // 通知事件类型(必选), 取值“121"表示历史媒体文件发送结束。
        String notifyType = XmlUtil.getTextOfChildTagFrom(rootElement, "NotifyType");
        if ("121".equals(notifyType)) {
            // 200 with no response body
            send200Response(event);
            // TODO 是否通知前端历史文件发送完成
        }
    }
}
