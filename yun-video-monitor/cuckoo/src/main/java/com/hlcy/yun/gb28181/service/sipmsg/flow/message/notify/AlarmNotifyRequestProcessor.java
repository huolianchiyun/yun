package com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify;

import com.hlcy.yun.gb28181.bean.DeviceInfo;
import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.AlarmEvent;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

@Slf4j
public class AlarmNotifyRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Alarm> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        // TODO  设备告警后续看怎么处理，先遗留
        DeviceInfo device = new DeviceInfo().setDeviceId(deviceId).setDeviceName(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceName"))
                .setManufacturer(XmlUtil.getTextOfChildTagFrom(rootElement, "Manufacturer"))
                .setModel(XmlUtil.getTextOfChildTagFrom(rootElement, "Model"))
                .setFirmware(XmlUtil.getTextOfChildTagFrom(rootElement, "Firmware"));
        // 向管理中心发布设备告警事件
        PublisherFactory.getDeviceEventPublisher().publishEvent(new AlarmEvent(device));
    }
}
