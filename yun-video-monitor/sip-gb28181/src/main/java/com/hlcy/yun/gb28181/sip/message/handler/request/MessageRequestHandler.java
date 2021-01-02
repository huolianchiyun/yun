package com.hlcy.yun.gb28181.sip.message.handler.request;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.DeviceChannel;
import com.hlcy.yun.gb28181.bean.RecordInfo;
import com.hlcy.yun.gb28181.bean.RecordItem;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.notification.PublisherFactory;
import com.hlcy.yun.gb28181.notification.event.KeepaliveEvent;
import com.hlcy.yun.gb28181.operation.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.util.XmlUtil;
import com.hlcy.yun.gb28181.bean.Device;
import com.hlcy.yun.gb28181.notification.event.AlarmEvent;
import com.hlcy.yun.gb28181.notification.event.DeviceEvent;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sip.RequestEvent;
import javax.sip.message.Request;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.hlcy.yun.gb28181.sip.message.handler.request.MessageRequestHandler.CmdTypeStrategyFactory.*;


@Slf4j
public class MessageRequestHandler extends RequestHandler {
    private final CmdTypeStrategyFactory strategyFactory = new CmdTypeStrategyFactory();

    @Override
    public void handle(RequestEvent event) {
        if (!Request.MESSAGE.equals(event.getRequest().getMethod())) {
            this.next.handle(event);
            return;
        }

        log.info("Receive a message request: {}", event.getRequest());
        strategyFactory.getCmdTypeStrategy(getCmdTypeFrom(event)).handleMessage(event);
    }

    private static Element getRootElementFrom(RequestEvent event) {
        Request request = event.getRequest();
        SAXReader reader = new SAXReader();
        reader.setEncoding("gbk");
        Document xml;
        try {
            xml = reader.read(new ByteArrayInputStream(request.getRawContent()));
        } catch (DocumentException e) {
            log.error("Parse a message request({}) XML-Content Exception, cause: {}.", event, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(String.format("Parse a message request(%s) XML-Content Exception", event.getRequest()), e);
        }
        return xml.getRootElement();
    }

    private String getCmdTypeFrom(RequestEvent event) {
        return XmlUtil.getTextOfChildTagFrom(getRootElementFrom(event), "CmdType");
    }

    class CmdTypeStrategyFactory {
        static final String MESSAGE_KEEP_ALIVE = "Keepalive";
        static final String MESSAGE_CONFIG_DOWNLOAD = "ConfigDownload";
        static final String MESSAGE_CATALOG = "Catalog";
        static final String MESSAGE_DEVICE_INFO = "DeviceInfo";
        static final String MESSAGE_ALARM = "Alarm";
        static final String MESSAGE_RECORD_INFO = "RecordInfo";

        private Map<String, AbstractCmdTypeStrategy> strategyMap = new HashMap<>();

        {
            strategyMap.put(MESSAGE_KEEP_ALIVE, new KeepaliveStrategy());
            strategyMap.put(MESSAGE_CONFIG_DOWNLOAD, new ConfigDownloadStrategy());
            strategyMap.put(MESSAGE_CATALOG, new CatalogStrategy());
            strategyMap.put(MESSAGE_DEVICE_INFO, new DeviceInfoStrategy());
            strategyMap.put(MESSAGE_ALARM, new AlarmStrategy());
            strategyMap.put(MESSAGE_RECORD_INFO, new RecordInfoStrategy());
        }

        AbstractCmdTypeStrategy getCmdTypeStrategy(String cmdType) {
            return strategyMap.get(cmdType);
        }
    }

    abstract static class AbstractCmdTypeStrategy {
        public abstract String getCmdType();

        protected abstract void handleMessage(RequestEvent event);
    }

    class KeepaliveStrategy extends AbstractCmdTypeStrategy {

        @Override
        public String getCmdType() {
            return MESSAGE_KEEP_ALIVE;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <KeepAlive> request: {}.", event.getRequest());
            try {
                Element rootElement = getRootElementFrom(event);
                String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
                MessageRequestHandler.this.sendAckResponse(event);
                PublisherFactory.getDeviceEventPublisher().publishEvent(new KeepaliveEvent(deviceId));
            } catch (ParseException e) {
                log.error("Handle a CmdType <KeepAlive> message({}) failed, cause: {}.", event.getRequest(), e.getMessage());
                e.printStackTrace();
            }
        }
    }

    static class ConfigDownloadStrategy extends AbstractCmdTypeStrategy {

        @Override
        public String getCmdType() {
            return MESSAGE_CONFIG_DOWNLOAD;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <ConfigDownload> request, message: {}.", event.getRequest());
        }
    }

    class CatalogStrategy extends AbstractCmdTypeStrategy {

        @Override
        public String getCmdType() {
            return MESSAGE_CATALOG;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <Catalog> request, message: {}.", event.getRequest());
            Element rootElement = getRootElementFrom(event);
            String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
            final Device device = new Device(deviceId);
            setChannelMapForDevice(device, rootElement.element("DeviceList"));
            PublisherFactory.getDeviceEventPublisher().publishEvent(new DeviceEvent(device));
            DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_CATALOG + deviceId, device);
            try {
                // 200 with no response body
                MessageRequestHandler.this.sendAckResponse(event);
            } catch (ParseException e) {
                log.error("Handle a CmdType <Catalog> message({}) failed, cause: {}.", event.getRequest(), e.getMessage());
                e.printStackTrace();
            }
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
                        .setStatus(Integer.parseInt(XmlUtil.getOrDefaultTextOf(itemDevice.element("Status"), "1")))
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

    static class DeviceInfoStrategy extends AbstractCmdTypeStrategy {

        @Override
        public String getCmdType() {
            return MESSAGE_DEVICE_INFO;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <DeviceInfo> request, message: {}.", event.getRequest());
            Element rootElement = getRootElementFrom(event);
            String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
            Device device = new Device(deviceId)
                    .setName(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceName"))
                    .setManufacturer(XmlUtil.getTextOfChildTagFrom(rootElement, "Manufacturer"))
                    .setModel(XmlUtil.getTextOfChildTagFrom(rootElement, "Model"))
                    .setFirmware(XmlUtil.getTextOfChildTagFrom(rootElement, "Firmware"));
            // 向管理中心发布设备事件
            PublisherFactory.getDeviceEventPublisher().publishEvent(new DeviceEvent(device));
            DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_DEVICE_INFO + deviceId, device);
        }
    }

    static class AlarmStrategy extends AbstractCmdTypeStrategy {

        @Override
        public String getCmdType() {
            return MESSAGE_ALARM;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <Alarm> request, message: {}.", event.getRequest());

            Element rootElement = getRootElementFrom(event);
            String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
            // TODO  设备告警后续看怎么处理，先遗留
            Device device = new Device(deviceId).setName(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceName"))
                    .setManufacturer(XmlUtil.getTextOfChildTagFrom(rootElement, "Manufacturer"))
                    .setModel(XmlUtil.getTextOfChildTagFrom(rootElement, "Model"))
                    .setFirmware(XmlUtil.getTextOfChildTagFrom(rootElement, "Firmware"));
            // 向管理中心发布设备告警事件
            PublisherFactory.getDeviceEventPublisher().publishEvent(new AlarmEvent(device));
        }
    }

    static class RecordInfoStrategy extends AbstractCmdTypeStrategy {
        private final static String CACHE_RECORD_INFO_KEY = "CACHE_RECORD_INFO_";
        private final static TimedCache<String, List<RecordItem>> cache = CacheUtil.newTimedCache(60 * 1000);

        @Override
        public String getCmdType() {
            return MESSAGE_RECORD_INFO;
        }

        @Override
        protected void handleMessage(RequestEvent event) {
            log.info("Receive a CmdType <RecordInfo> request, message: {}.", event.getRequest());
            Element rootElement = getRootElementFrom(event);
            final String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
            RecordInfo recordInfo = extractRecordInfoFrom(rootElement);
            final String sn = XmlUtil.getTextOfChildTagFrom(rootElement, "SN");
            if (isFullRecordList(recordInfo, sn)) {
                DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_RECORD_INFO + deviceId, recordInfo);
            }
        }

        private boolean isFullRecordList(RecordInfo recordInfo, String sn) {
            final List<RecordItem> recordList = recordInfo.getRecordList();
            // 存在录像且如果当前录像明细个数小于总条数，说明拆包返回，需要组包，暂不返回
            if (recordInfo.getSumNum() > 0 && recordList.size() > 0 && recordList.size() < recordInfo.getSumNum()) {
                // 为防止连续请求该设备的录像数据，返回数据错乱，特增加sn(命令序列号)进行区分
                String cacheKey = CACHE_RECORD_INFO_KEY + recordInfo.getDeviceId() + ":" + sn;
                final List<RecordItem> recordItemListCache = cache.get(cacheKey);
                if (recordItemListCache != null) {
                    if (recordItemListCache.size() > 0) {
                        recordList.addAll(recordItemListCache);
                    }
                    // 本分支表示录像列表被拆包，且加上之前的数据还是不够,保存缓存返回，等待下个包再处理
                    if (recordList.size() < recordInfo.getSumNum()) {
                        cache.put(cacheKey, recordList);
                        return false;
                    } else {
                        // 本分支表示录像被拆包，但加上之前的数据够足够，返回响应，为省内存，此处手动删除
                        cache.remove(cacheKey);
                        return true;
                    }
                } else {
                    /* 本分支有两种可能：
                     1、录像列表被拆包，且是第一个包,直接保存缓存返回，等待下个包再处理
                     2、之前有包，但超时清空了，那么这次 sn 批次的响应数据已经不完整，等待过期时间后 redis 自动清空数据*/
                    cache.put(cacheKey, recordList);
                    return false;
                }
            }
             /*走到这里，有以下可能：
             1、没有录像信息,第一次收到 record info 的消息即返回响应数据，无 redis 操作
             2、有录像数据，且第一次即收到完整数据，返回响应数据，无 redis 操作
             3、有录像数据，在超时时间内收到多次包组装后数量足够，返回数据*/
            return true;
        }

        private RecordInfo extractRecordInfoFrom(Element rootElement) {
            RecordInfo recordInfo = new RecordInfo()
                    .setDeviceId(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID"))
                    .setName(XmlUtil.getTextOfChildTagFrom(rootElement, "Name"))
                    .setSumNum(Integer.parseInt(XmlUtil.getTextOfChildTagFrom(rootElement, "SumNum")));

            Element recordListElement = rootElement.element("RecordList");
            JUMP_END:
            {
                if (recordListElement == null) break JUMP_END;
                List<RecordItem> recordList = new ArrayList<>();
                Iterator<Element> recordListIterator = recordListElement.elementIterator();
                if (recordListIterator != null) {
                    while (recordListIterator.hasNext()) {
                        Element recordItem = recordListIterator.next();
                        if (recordItem.element("DeviceID") == null) continue;
                        recordList.add(new RecordItem()
                                .setDeviceId(XmlUtil.getTextOfChildTagFrom(recordItem, "DeviceID"))
                                .setName(XmlUtil.getTextOfChildTagFrom(recordItem, "Name"))
                                .setFilePath(XmlUtil.getTextOfChildTagFrom(recordItem, "FilePath"))
                                .setAddress(XmlUtil.getTextOfChildTagFrom(recordItem, "Address"))
                                .setStartTime(LocalDateTime.parse(XmlUtil.getTextOfChildTagFrom(recordItem, "StartTime"))
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .setEndTime(LocalDateTime.parse(XmlUtil.getTextOfChildTagFrom(recordItem, "EndTime"))
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .setSecrecy(Integer.parseInt(XmlUtil.getOrDefaultTextOf(recordItem.element("Secrecy"), "0")))
                                .setType(XmlUtil.getTextOfChildTagFrom(recordItem, "Type"))
                                .setRecorderId(XmlUtil.getTextOfChildTagFrom(recordItem, "RecorderID")));
                    }
                    recordInfo.setRecordList(recordList);
                }
            }
            return recordInfo;
        }
    }
}
