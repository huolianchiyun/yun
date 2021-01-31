package com.hlcy.yun.gb28181.service.params.notify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceBroadcastNotifyParams extends NotifyParams {
    public VoiceBroadcastNotifyParams() {
        super("Broadcast");
    }
}
