package com.hlcy.yun.gb28181.operation.flow;

import com.hlcy.yun.gb28181.sip.message.DefaultPipeline;
import com.hlcy.yun.gb28181.operation.ResponseProcessor;
import com.hlcy.yun.gb28181.operation.flow.palyer.play.*;
import javax.sip.ResponseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static com.hlcy.yun.gb28181.operation.flow.Operation.PLAY;
import static com.hlcy.yun.gb28181.operation.flow.Operation.PLAYBACK;

class FlowPipelineFactory {
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

        DefaultPipeline<ResponseProcessor, ResponseEvent> PLAYBACK_PIPELINE = new DefaultPipeline<>();
        PLAY_PIPELINE.addLast("3-4", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.MediaInviteResponseProcessor1());
        PLAY_PIPELINE.addLast("5-6-7-8", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.DeviceInviteResponseProcessor());
        PLAY_PIPELINE.addLast("9-10-11-12", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.MediaInviteResponseProcessor2());
        PLAY_PIPELINE.addLast("16-17", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.MediaByeResponseProcessor1());
        PLAY_PIPELINE.addLast("18-19", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.MediaByeResponseProcessor2());
        PLAY_PIPELINE.addLast("20", new com.hlcy.yun.gb28181.operation.flow.palyer.playback.DeviceByeResponseProcessor());
        map.put(PLAYBACK, PLAYBACK_PIPELINE);


        PIPELINE_CONTAINER = Collections.unmodifiableMap(map);
    }

    static DefaultPipeline<ResponseProcessor, ResponseEvent> getFlowPipeline(Operation operation) {
        return PIPELINE_CONTAINER.getOrDefault(operation, null);
    }
}
