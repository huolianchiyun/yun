package com.hlcy.yun.gb28181.service.sipmsg.flow.message.query;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.hlcy.yun.gb28181.bean.RecordInfo;
import com.hlcy.yun.gb28181.bean.RecordItem;
import com.hlcy.yun.gb28181.service.sipmsg.callback.DeferredResultHolder;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.MessageProcessor;
import com.hlcy.yun.gb28181.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 文件目录检索请求处理器
 */
@Slf4j
public class RecordInfoQueryProcessor extends MessageProcessor {
    private final static String CACHE_RECORD_INFO_KEY = "CACHE_RECORD_INFO_";
    private final TimedCache<String, List<RecordItem>> cache = CacheUtil.newTimedCache(60 * 1000);

    @Override
    protected void doProcess(RequestEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Receive a CmdType <RecordInfo> request, message: \n{}", event.getRequest());
        }
        Element rootElement = getRootElementFrom(event);
        final String deviceId = XmlUtil.getTextOfChildTagFrom(rootElement, "DeviceID");
        RecordInfo recordInfo = extractRecordInfoFrom(rootElement);
        final String sn = XmlUtil.getTextOfChildTagFrom(rootElement, "SN");
        if (isFullRecordList(recordInfo, sn)) {
            DeferredResultHolder.setDeferredResultForRequest(DeferredResultHolder.CALLBACK_CMD_RECORD_INFO + deviceId, recordInfo);
        }
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
                     2、之前有包，但超时清空了，那么这次 sn 批次的响应数据已经不完整，等待过期时间后 cache 自动清空数据*/
                cache.put(cacheKey, recordList);
                return false;
            }
        }
             /*走到这里，有以下可能：
             1、没有录像信息,第一次收到 record info 的消息即返回响应数据，无 cache 操作
             2、有录像数据，且第一次即收到完整数据，返回响应数据，无 cache 操作
             3、有录像数据，在超时时间内收到多次包组装后数量足够，返回数据*/
        return true;
    }
}
