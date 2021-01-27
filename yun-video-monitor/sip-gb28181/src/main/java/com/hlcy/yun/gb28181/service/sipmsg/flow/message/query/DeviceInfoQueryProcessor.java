package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

/**
 * 设备信息查询请求处理器
 */
@Slf4j
public class DeviceInfoQueryProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <DeviceInfo> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        Device device = new Device(deviceId)
                .setName(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceName"))
                .setManufacturer(XmlUtil.getTextOfChildTagFrom(rootElement, "Manufacturer"))
                .setModel(XmlUtil.getTextOfChildTagFrom(rootElement, "Model"))
                .setFirmware(XmlUtil.getTextOfChildTagFrom(rootElement, "Firmware"));

        DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_DEVICE_INFO + deviceId, device);
    }
}
