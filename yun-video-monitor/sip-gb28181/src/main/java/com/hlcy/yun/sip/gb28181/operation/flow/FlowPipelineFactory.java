package com.hlcy.yun.sip.gb28181.operation.flow;

import com.hlcy.yun.sip.gb28181.message.DefaultPipeline;
import com.hlcy.yun.sip.gb28181.operation.Operation;
import com.hlcy.yun.sip.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.sip.gb28181.operation.flow.play.*;
import javax.sip.ResponseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.hlcy.yun.sip.gb28181.operation.Operation.PLAY;

public class FlowPipelineFactory {
    private static final Map<Operation, DefaultPipeline<ResponseProcessor, ResponseEvent>> PIPELINE_CONTAINER;

    static {
        final Map<Operation, DefaultPipeline<ResponseProcessor, ResponseEvent>> map = new HashMap<>();
        DefaultPipeline<ResponseProcessor, ResponseEvent> PLAY_PIPELINE = new DefaultPipeline<>();
        PLAY_PIPELINE.addLast("3-4", new MediaInviteResponseProcessor1());
        PLAY_PIPELINE.addLast("5-6-7-8", new DeviceInviteResponseProcessor());
        PLAY_PIPELINE.addLast("9-10-11-12", new MediaInviteResponseProcessor2());
        PLAY_PIPELINE.addLast("16-17", new MediaByeResponseProcessor1());
        PLAY_PIPELINE.addLast("18-19", new MediaByeResponseProcessor2());
        PLAY_PIPELINE.addLast("20", new DeviceByeResponseProcessor());
        map.put(PLAY, PLAY_PIPELINE);
        PIPELINE_CONTAINER = Collections.unmodifiableMap(map);
    }

    public static DefaultPipeline<ResponseProcessor, ResponseEvent> getFlowPipeline(Operation operation) {
        return PIPELINE_CONTAINER.getOrDefault(operation, null);
    }
}
