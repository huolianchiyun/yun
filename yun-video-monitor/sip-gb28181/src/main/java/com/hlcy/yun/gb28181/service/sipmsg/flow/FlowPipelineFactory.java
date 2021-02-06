package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.*;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.KeepaliveNotifyProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.DeviceInfoQueryProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.RecordInfoQueryProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.*;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.CatalogQueryProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.DeviceByeResponseProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessor;
import com.hlcy.yun.gb28181.sip.biz.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.message.DefaultPipeline;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
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

        // Control

        // Query
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> CATALOG_PIPELINE = new DefaultPipeline<>();
        CATALOG_PIPELINE.addLast(CATALOG.code(), new CatalogQueryProcessor());
        requestMap.put(CATALOG, CATALOG_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> DEVICE_INFO_PIPELINE = new DefaultPipeline<>();
        DEVICE_INFO_PIPELINE.addLast(DEVICE_INFO.code(), new DeviceInfoQueryProcessor());
        requestMap.put(DEVICE_INFO, DEVICE_INFO_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> RECORD_INFO_PIPELINE = new DefaultPipeline<>();
        RECORD_INFO_PIPELINE.addLast(RECORD_INFO.code(), new RecordInfoQueryProcessor());
        requestMap.put(RECORD_INFO, RECORD_INFO_PIPELINE);

        // Notify
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> KEEPALIVE_PIPELINE = new DefaultPipeline<>();
        KEEPALIVE_PIPELINE.addLast(KEEPALIVE.code(), new KeepaliveNotifyProcessor());
        requestMap.put(KEEPALIVE, KEEPALIVE_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> VOICE_BROADCAST_PIPELINE = new DefaultPipeline<>();
        VOICE_BROADCAST_PIPELINE.addLast(BROADCAST.code(), new BroadcastNotifyProcessor());
        VOICE_BROADCAST_PIPELINE.addLast("invite", new DeviceInviteRequestProcessor());
        VOICE_BROADCAST_PIPELINE.addLast("ack", new DeviceAckRequestProcessor());
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
        PLAY_PIPELINE.addLast(PLAY.name(), new MediaByeResponseProcessor1());  // "16-17"
        PLAY_PIPELINE.addLast("18-19", new MediaByeResponseProcessor2());
        PLAY_PIPELINE.addLast("20", new DeviceByeResponseProcessor());
        responseMap.put(PLAY, PLAY_PIPELINE);

        // Playback
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> PLAYBACK_PIPELINE = new DefaultPipeline<>();
        PLAYBACK_PIPELINE.addLast("3-4", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor1());
        PLAYBACK_PIPELINE.addLast("5-6-7-8", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceInviteResponseProcessor());
        PLAYBACK_PIPELINE.addLast("9-10-11-12", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor2());
        PLAYBACK_PIPELINE.addLast(PLAYBACK.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor1());  // "24-25"
        PLAYBACK_PIPELINE.addLast("26-27", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor2());
        PLAYBACK_PIPELINE.addLast("28", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceByeResponseProcessor());
        responseMap.put(PLAYBACK, PLAYBACK_PIPELINE);

        // Broadcast
        DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> VOICE_BROADCAST_PIPELINE = new DefaultPipeline<>();
        VOICE_BROADCAST_PIPELINE.addLast("7-8-9-10-11-12-13-14", new MediaInviteResponseProcessor());
        VOICE_BROADCAST_PIPELINE.addLast(BROADCAST.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.broadcast.DeviceByeResponseProcessor());  // "18-19"
        VOICE_BROADCAST_PIPELINE.addLast("20-21-22-23", new MediaByeResponseProcessor());
        responseMap.put(BROADCAST, VOICE_BROADCAST_PIPELINE);

        RESPONSE_PIPELINE_CONTAINER = Collections.unmodifiableMap(responseMap);
    }

    public static DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> getRequestFlowPipeline(Operation operation) {
        return REQUEST_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }

    static DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> getResponseFlowPipeline(Operation operation) {
        return RESPONSE_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }
}
