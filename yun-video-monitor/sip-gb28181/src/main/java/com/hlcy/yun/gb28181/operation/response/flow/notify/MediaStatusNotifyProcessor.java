package com.hlcy.yun.gb28181.operation.response.flow.notify;

import com.hlcy.yun.gb28181.operation.response.flow.query.MessageQueryProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.text.ParseException;

@Slf4j
public class MediaStatusNotifyProcessor extends MessageQueryProcessor {

    @Override
    protected void process(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <MediaStatus> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");

        // 通知事件类型(必选), 取值“121"表示历史媒体文件发送结束。
        String notifyType = XmlUtil.getTextOfChildTagFrom(rootElement, "NotifyType");
        if ("121".equals(notifyType)) {
            try {
                // 200 with no response body
                send200Response(event);
            } catch (ParseException e) {
                log.error("Handle a CmdType <MediaStatus> message({}) failed, cause: {}.", event.getRequest(), e.getMessage());
                e.printStackTrace();
            }
            // TODO 是否通知前端历史文件发送完成
        }
    }


}
