package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import com.hlcy.yun.gb28181.bean.DeviceInfo;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;

import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

/**
 * 设备信息查询请求处理器
 */
@Slf4j
public class DeviceInfoQueryRequestProcessor extends MessageRequestProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <DeviceInfo> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
        DeviceInfo device = new DeviceInfo()
                .setDeviceId(deviceId)
                .setDeviceName(getTextOfChildTagFrom(rootElement, "DeviceName"))
                .setManufacturer(getTextOfChildTagFrom(rootElement, "Manufacturer"))
                .setModel(getTextOfChildTagFrom(rootElement, "Model"))
                .setFirmware(getTextOfChildTagFrom(rootElement, "Firmware"));

        DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_QUERY_DEVICE_INFO + deviceId, device);
    }
}
