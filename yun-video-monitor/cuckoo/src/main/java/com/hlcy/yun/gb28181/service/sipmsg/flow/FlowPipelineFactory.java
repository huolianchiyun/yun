package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.AlarmNotifyRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.MediaStatusNotifyRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.*;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.KeepaliveNotifyRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.DeviceInfoQueryRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.RecordInfoQueryRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.subscribe.SubscribeResponseProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.*;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.CatalogQueryRequestProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.DeviceByeResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.message.DefaultPipeline;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.message.Request;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.*;

public class FlowPipelineFactory {

    private static Map<Operation, DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent>> REQUEST_PIPELINE_CONTAINER;

    private static Map<Operation, DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent>> RESPONSE_PIPELINE_CONTAINER;

    static {
        initRequestPipelineContainer();
        initResponsePipelineContainer();
    }

    private static void initRequestPipelineContainer() {
        final Map<Operation, DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent>> requestMap = new HashMap<>();
        // Query
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> CATALOG_PIPELINE = new DefaultPipeline<>();
        CATALOG_PIPELINE.addLast(CATALOG.code(), new CatalogQueryRequestProcessor());
        requestMap.put(CATALOG, CATALOG_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> DEVICE_INFO_PIPELINE = new DefaultPipeline<>();
        DEVICE_INFO_PIPELINE.addLast(DEVICE_INFO.code(), new DeviceInfoQueryRequestProcessor());
        requestMap.put(DEVICE_INFO, DEVICE_INFO_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> RECORD_INFO_PIPELINE = new DefaultPipeline<>();
        RECORD_INFO_PIPELINE.addLast(RECORD_INFO.code(), new RecordInfoQueryRequestProcessor());
        requestMap.put(RECORD_INFO, RECORD_INFO_PIPELINE);

        // Notify
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> KEEPALIVE_PIPELINE = new DefaultPipeline<>();
        KEEPALIVE_PIPELINE.addLast(KEEPALIVE.code(), new KeepaliveNotifyRequestProcessor());
        requestMap.put(KEEPALIVE, KEEPALIVE_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> MEDIA_STATUS_PIPELINE = new DefaultPipeline<>();
        MEDIA_STATUS_PIPELINE.addLast(MEDIA_STATUS.code(), new MediaStatusNotifyRequestProcessor());
        requestMap.put(MEDIA_STATUS, MEDIA_STATUS_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> ALARM_NOTIFY_PIPELINE = new DefaultPipeline<>();
        ALARM_NOTIFY_PIPELINE.addLast(ALARM.code(), new AlarmNotifyRequestProcessor());
        requestMap.put(ALARM, ALARM_NOTIFY_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> VOICE_BROADCAST_PIPELINE = new DefaultPipeline<>();
        VOICE_BROADCAST_PIPELINE.addLast(BROADCAST.code(), new BroadcastNotifyRequestProcessor());
        VOICE_BROADCAST_PIPELINE.addLast(Request.INVITE, new DeviceInviteRequestProcessor());
        VOICE_BROADCAST_PIPELINE.addLast(Request.ACK, new DeviceAckRequestProcessor());
        // 支持设备主动发 bye
        VOICE_BROADCAST_PIPELINE.addLast(Request.BYE, new DeviceByeRequestProcessor());
        requestMap.put(BROADCAST, VOICE_BROADCAST_PIPELINE);

        REQUEST_PIPELINE_CONTAINER = Collections.unmodifiableMap(requestMap);
    }

    private static void initResponsePipelineContainer() {
        final Map<Operation, DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent>> responseMap = new HashMap<>();
        // Play
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> PLAY_PIPELINE = new DefaultPipeline<>();
        PLAY_PIPELINE.addLast("3-4", new MediaInviteResponseProcessor1());
        PLAY_PIPELINE.addLast("5-6-7-8", new DeviceInviteResponseProcessor());
        PLAY_PIPELINE.addLast("9-10-11-12", new MediaInviteResponseProcessor2());
        // 16-17
        PLAY_PIPELINE.addLast(PLAY.name(), new MediaByeResponseProcessor1());
        PLAY_PIPELINE.addLast("18-19", new MediaByeResponseProcessor2());
        // 20
        PLAY_PIPELINE.addLast("DeviceByResponseProcessor", new DeviceByeResponseProcessor());
        responseMap.put(PLAY, PLAY_PIPELINE);

        // Playback
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> PLAYBACK_PIPELINE = new DefaultPipeline<>();
        PLAYBACK_PIPELINE.addLast("3-4", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor1());
        PLAYBACK_PIPELINE.addLast("5-6-7-8", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceInviteResponseProcessor());
        PLAYBACK_PIPELINE.addLast("9-10-11-12", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor2());
        // 24-25
        PLAYBACK_PIPELINE.addLast(PLAYBACK.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor1());
        PLAYBACK_PIPELINE.addLast("26-27", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor2());
        PLAYBACK_PIPELINE.addLast("DeviceByResponseProcessor", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceByeResponseProcessor());
        responseMap.put(PLAYBACK, PLAYBACK_PIPELINE);

        // History video download
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> DOWNLOAD_PIPELINE = new DefaultPipeline<>();
        DOWNLOAD_PIPELINE.addLast("3-4", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.MediaInviteResponseProcessor1());
        DOWNLOAD_PIPELINE.addLast("5-6-7-8", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.DeviceInviteResponseProcessor());
        DOWNLOAD_PIPELINE.addLast("9-10-11-12", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.MediaInviteResponseProcessor2());
        // 24-25
        DOWNLOAD_PIPELINE.addLast(PLAYBACK.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.MediaByeResponseProcessor1());
        DOWNLOAD_PIPELINE.addLast("26-27", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.MediaByeResponseProcessor2());
        DOWNLOAD_PIPELINE.addLast("DeviceByResponseProcessor", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.download.DeviceByeResponseProcessor());
        responseMap.put(DOWNLOAD, DOWNLOAD_PIPELINE);

        // Broadcast
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> VOICE_BROADCAST_PIPELINE = new DefaultPipeline<>();
        VOICE_BROADCAST_PIPELINE.addLast("7-8-9-10-11-12-13-14", new MediaInviteResponseProcessor());
        // 18-19
        VOICE_BROADCAST_PIPELINE.addLast(BROADCAST.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.DeviceByeResponseProcessor());
        VOICE_BROADCAST_PIPELINE.addLast("20-21-22-23", new MediaByeResponseProcessor());
        responseMap.put(BROADCAST, VOICE_BROADCAST_PIPELINE);

        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> SUBSCRIBE_PIPELINE = new DefaultPipeline<>();
        SUBSCRIBE_PIPELINE.addLast(SUBSCRIBE.code(), new SubscribeResponseProcessor());
        responseMap.put(SUBSCRIBE, SUBSCRIBE_PIPELINE);

        RESPONSE_PIPELINE_CONTAINER = Collections.unmodifiableMap(responseMap);
    }

    public static DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> getRequestFlowPipeline(Operation operation) {
        return REQUEST_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }

    public static DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> getResponseFlowPipeline(Operation operation) {
        return RESPONSE_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }
}
