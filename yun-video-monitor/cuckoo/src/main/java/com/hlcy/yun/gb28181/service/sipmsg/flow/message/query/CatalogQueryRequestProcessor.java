package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.Catalog;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageRequestProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.util.*;

import static com.hlcy.yun.gb28181.util.XmlUtil.getOrDefaultTextOf;
import static com.hlcy.yun.gb28181.util.XmlUtil.getTextOfChildTagFrom;

/**
 * 设备目录信息查询请求处理器
 */
@Slf4j
public class CatalogQueryRequestProcessor extends MessageRequestProcessor {
    private final static String CACHE_CATALOG_KEY = "CACHE_Catalog_";
    private final TimedCache<String, List<Catalog.Channel>> cache = CacheUtil.newTimedCache(60 * 1000);

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <Catalog> request, message: {}.", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);

        if (!isMessageError(rootElement)) {
            final Catalog catalog = extractCatalogFrom(rootElement);
            final String sn = getTextOfChildTagFrom(rootElement, "SN");
            if (isFullDeviceList(catalog, sn)) {
                DeferredResultHolder.setDeferredResultForRequest(
                        DeferredResultHolder.CALLBACK_CMD_QUERY_CATALOG + getTextOfChildTagFrom(rootElement, "DeviceID"), catalog);
            }
        }
    }

    private boolean isMessageError(Element rootElement) {
        final String result = getTextOfChildTagFrom(rootElement, "Result");
        if (CLIENT_RESPONSE_REQUEST_RESULT_ERROR.equalsIgnoreCase(result)) {
            String deviceId = getTextOfChildTagFrom(rootElement, "DeviceID");
            DeferredResultHolder.setErrorDeferredResultForRequest(
                    DeferredResultHolder.CALLBACK_CMD_QUERY_CATALOG + deviceId,
                    getTextOfChildTagFrom(rootElement, "Reason"));
            cache.remove(CACHE_CATALOG_KEY + deviceId + ":" + getTextOfChildTagFrom(rootElement, "SN"));
            return true;
        }
        return false;
    }

    private Catalog extractCatalogFrom(Element rootElement) {
        Catalog catalog = new Catalog()
                .setDeviceId(XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID"))
                .setSumNum(Integer.parseInt(XmlUtil.getTextOfChildTagFrom(rootElement, "SumNum")));

        final Element deviceListElement = rootElement.element("DeviceList");
        JUMP_END:
        {
            if (deviceListElement == null) break JUMP_END;
            List<Catalog.Channel> channels = new ArrayList<>();
            Iterator<Element> channelListIterator = deviceListElement.elementIterator();
            if (channelListIterator != null) {
                while (channelListIterator.hasNext()) {
                    Element channelItem = channelListIterator.next();
                    final Element channelIdElement = channelItem.element("DeviceID");
                    if (channelIdElement == null) continue;
                    channels.add(new Catalog.Channel()
                            .setChannelName(XmlUtil.getTextOf(channelItem.element("Name")))
                            .setChannelId(channelIdElement.getText())
                            .setStatus(getOrDefaultTextOf(channelItem.element("Status"), "ON").equalsIgnoreCase("ON") ? 1 : 0)
                            .setManufacturer(getTextOfChildTagFrom(channelItem, "Manufacturer"))
                            .setModel(getTextOfChildTagFrom(channelItem, "Model"))
                            .setOwner(getTextOfChildTagFrom(channelItem, "Owner"))
                            .setCivilCode(getTextOfChildTagFrom(channelItem, "CivilCode"))
                            .setBlock(getTextOfChildTagFrom(channelItem, "Block"))
                            .setAddress(getTextOfChildTagFrom(channelItem, "Address"))
                            .setParental(Integer.parseInt(getOrDefaultTextOf(channelItem.element("Parental"), "0")))
                            .setParentId(getTextOfChildTagFrom(channelItem, "ParentId"))
                            .setSafetyWay(Integer.parseInt(getOrDefaultTextOf(channelItem.element("SafetyWay"), "0")))
                            .setRegisterWay(Integer.parseInt(getOrDefaultTextOf(channelItem.element("RegisterWay"), "1")))
                            .setCertNum(getTextOfChildTagFrom(channelItem, "CertNum"))
                            .setCertifiable(Integer.parseInt(getOrDefaultTextOf(channelItem.element("Certifiable"), "0")))
                            .setErrCode(Integer.parseInt(getOrDefaultTextOf(channelItem.element("ErrCode"), "0")))
                            .setEndTime(getTextOfChildTagFrom(channelItem, "EndTime"))
                            .setSecrecy(getTextOfChildTagFrom(channelItem, "Secrecy"))
                            .setIpAddress(getTextOfChildTagFrom(channelItem, "IPAddress"))
                            .setPort(Integer.parseInt(getOrDefaultTextOf(channelItem.element("Port"), "0")))
                            .setPassword(getTextOfChildTagFrom(channelItem, "Password"))
                            .setLongitude(Double.parseDouble(getOrDefaultTextOf(channelItem.element("Longitude"), "0.00")))
                            .setLatitude(Double.parseDouble(getOrDefaultTextOf(channelItem.element("Latitude"), "0.00"))));
                }
                catalog.setChannels(channels);
            }
        }
        return catalog;
    }

    private boolean isFullDeviceList(Catalog catalog, String sn) {
        final List<Catalog.Channel> deviceList = catalog.getChannels();
        // 存在通道且如果当前通道明细个数小于总条数，说明拆包返回，需要组包，暂不返回
        if (catalog.getSumNum() > 0 && deviceList.size() > 0 && deviceList.size() < catalog.getSumNum()) {
            // 为防止连续请求该设备的通道数据，返回数据错乱，特增加sn(命令序列号)进行区分
            String cacheKey = CACHE_CATALOG_KEY + catalog.getDeviceId() + ":" + sn;
            final List<Catalog.Channel> channelItemListCache = cache.get(cacheKey);
            if (channelItemListCache != null) {
                if (channelItemListCache.size() > 0) {
                    deviceList.addAll(channelItemListCache);
                }
                // 本分支表示通道列表被拆包，且加上之前的数据还是不够,保存缓存返回，等待下个包再处理
                if (deviceList.size() < catalog.getSumNum()) {
                    cache.put(cacheKey, deviceList);
                    return false;
                } else {
                    // 本分支表示通道被拆包，但加上之前的数据够足够，返回响应，为省内存，此处手动删除
                    cache.remove(cacheKey);
                    return true;
                }
            } else {
                    /* 本分支有两种可能：
                     1、通道列表被拆包，且是第一个包,直接保存缓存返回，等待下个包再处理
                     2、之前有包，但超时清空了，那么这次 sn 批次的响应数据已经不完整，等待过期时间后 cache 自动清空数据*/
                cache.put(cacheKey, deviceList);
                return false;
            }
        }
             /*走到这里，有以下可能：
             1、没有通道信息,第一次收到 catalog 的消息即返回响应数据，无 cache 操作
             2、有通道数据，且第一次即收到完整数据，返回响应数据，无 cache 操作
             3、有通道数据，在超时时间内收到多次包组装后数量足够，返回数据*/
        return true;
    }
}
