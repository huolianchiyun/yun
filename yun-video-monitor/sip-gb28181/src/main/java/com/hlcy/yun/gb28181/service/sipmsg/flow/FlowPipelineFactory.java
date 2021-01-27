package com.hlcy.yun.gb28181.service.sipmsg.flow;

import com.hlcy.yun.gb28181.service.sipmsg.flow.message.notify.KeepaliveNotifyProcessor;
import com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.play.*;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.query.CatalogQueryProcessor;
import com.hlcy.yun.gb28181.sip.client.RequestProcessor;
import com.hlcy.yun.gb28181.sip.client.ResponseProcessor;
import com.hlcy.yun.gb28181.sip.message.DefaultPipeline;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.hlcy.yun.gb28181.service.sipmsg.flow.Operation.*;

public class FlowPipelineFactory {

    private static final Map<Operation, DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent>> REQUEST_PIPELINE_CONTAINER;

    private static final Map<Operation, DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent>> RESPONSE_PIPELINE_CONTAINER;

    static {
        final Map<Operation, DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent>> requestMap = new HashMap<>();
        // Query
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> CATALOG_PIPELINE = new DefaultPipeline<>();
        CATALOG_PIPELINE.addLast("Catalog", new CatalogQueryProcessor());
        requestMap.put(CATALOG, CATALOG_PIPELINE);

        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> DEVICE_INFO_PIPELINE = new DefaultPipeline<>();
        DEVICE_INFO_PIPELINE.addLast("DeviceInfo", new CatalogQueryProcessor());
        requestMap.put(DEVICE_INFO, DEVICE_INFO_PIPELINE);

        // Notify
        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> KEEPALIVE_PIPELINE = new DefaultPipeline<>();
        KEEPALIVE_PIPELINE.addLast("Keepalive", new KeepaliveNotifyProcessor());
        requestMap.put(KEEPALIVE, KEEPALIVE_PIPELINE);

        // Control
//        DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> _PIPELINE = new DefaultPipeline<>();
//        CATALOG_PIPELINE.addLast("Keepalive", new KeepaliveNotifyProcessor());
//        requestMap.put(KEEPALIVE, KEEPALIVE_PIPELINE);


        REQUEST_PIPELINE_CONTAINER = Collections.unmodifiableMap(requestMap);

        // Play
        final Map<Operation, DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent>> responseMap = new HashMap<>();
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
        PLAY_PIPELINE.addLast("3-4", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor1());
        PLAY_PIPELINE.addLast("5-6-7-8", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceInviteResponseProcessor());
        PLAY_PIPELINE.addLast("9-10-11-12", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaInviteResponseProcessor2());
        PLAY_PIPELINE.addLast(PLAYBACK.name(), new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor1());  // "24-25"
        PLAY_PIPELINE.addLast("26-27", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.MediaByeResponseProcessor2());
        PLAY_PIPELINE.addLast("28", new com.hlcy.yun.gb28181.service.sipmsg.flow.palyer.playback.DeviceByeResponseProcessor());
        responseMap.put(PLAYBACK, PLAYBACK_PIPELINE);

        RESPONSE_PIPELINE_CONTAINER = Collections.unmodifiableMap(responseMap);
    }

    public static DefaultPipeline<RequestProcessor<FlowContext>, RequestEvent> getRequestFlowPipeline(Operation operation) {
        return REQUEST_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }

    public static DefaultPipeline<ResponseProcessor<FlowContext>, ResponseEvent> getResponseFlowPipeline(Operation operation) {
        return RESPONSE_PIPELINE_CONTAINER.getOrDefault(operation, null);
    }
}
