package com.hlcy.yun.gb28181.operation.response.flow.message.query;

import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.bean.DeviceChannel;
import com.hlcy.yun.gb28181.operation.response.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.operation.response.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 报警查询请求处理器
 */
@Slf4j
public class AlarmQueryProcessor extends MessageProcessor {

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Catalog> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        final Device device = new Device(deviceId);
        setChannelMapForDevice(device, rootElement.element("DeviceList"));

        DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_CATALOG + deviceId, device);
    }

    private void setChannelMapForDevice(Device device, Element deviceListElement) {
        if (deviceListElement == null) return;
        Iterator<Element> deviceListIterator = deviceListElement.elementIterator();
        if (deviceListIterator == null) return;

        Map<String, DeviceChannel> channelMap = new HashMap<>(5);
        device.setChannelMap(channelMap);
        while (deviceListIterator.hasNext()) {
            Element itemDevice = deviceListIterator.next();
            Element channelDeviceElement = itemDevice.element("DeviceID");
            if (channelDeviceElement == null) continue;

            String channelDeviceId = channelDeviceElement.getText();
            channelMap.put(channelDeviceId, new DeviceChannel()
                    .setName(XmlUtil.getTextOf(itemDevice.element("Name")))
                    .setChannelId(channelDeviceId)
                    .setStatus(XmlUtil.getOrDefaultTextOf(itemDevice.element("Status"), "ON").equalsIgnoreCase("ON") ? 1 : 0)
                    .setManufacture(XmlUtil.getTextOfChildTagFrom(itemDevice, "Manufacturer"))
                    .setModel(XmlUtil.getTextOfChildTagFrom(itemDevice, "Model"))
                    .setOwner(XmlUtil.getTextOfChildTagFrom(itemDevice, "Owner"))
                    .setCivilCode(XmlUtil.getTextOfChildTagFrom(itemDevice, "CivilCode"))
                    .setBlock(XmlUtil.getTextOfChildTagFrom(itemDevice, "Block"))
                    .setAddress(XmlUtil.getTextOfChildTagFrom(itemDevice, "Address"))
                    .setParental(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("Parental"), "0")))
                    .setParentId(XmlUtil.getTextOfChildTagFrom(itemDevice, "ParentId"))
                    .setSafetyWay(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("SafetyWay"), "0")))
                    .setRegisterWay(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("RegisterWay"), "1")))
                    .setCertNum(XmlUtil.getTextOfChildTagFrom(itemDevice, "CertNum"))
                    .setCertifiable(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("Certifiable"), "0")))
                    .setErrCode(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("ErrCode"), "0")))
                    .setEndTime(XmlUtil.getTextOfChildTagFrom(itemDevice, "EndTime"))
                    .setSecrecy(XmlUtil.getTextOfChildTagFrom(itemDevice, "Secrecy"))
                    .setIpAddress(XmlUtil.getTextOfChildTagFrom(itemDevice, "IPAddress"))
                    .setPort(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("Port"), "0")))
                    .setPassword(XmlUtil.getTextOfChildTagFrom(itemDevice, "Password"))
                    .setLongitude(Double.parseDouble(XmlUtil.getOrDefaultTextOf(itemDevice.element("Longitude"), "0.00")))
                    .setLatitude(Double.parseDouble(XmlUtil.getOrDefaultTextOf(itemDevice.element("Latitude"), "0.00"))));
        }
    }
}
