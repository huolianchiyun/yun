package com.hlcy.yun.gb28181.service.command.control;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.params.control.DeviceConfigControlParams;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContext;
import com.hlcy.yun.gb28181.service.sipmsg.flow.FlowContextCacheUtil;
import com.hlcy.yun.gb28181.service.sipmsg.flow.Operation;
import com.hlcy.yun.gb28181.service.sipmsg.flow.message.DefaultMessageResponseProcessor;

import javax.sip.message.Request;

import static com.hlcy.yun.gb28181.sip.message.factory.SipRequestFactory.getCallId;

/**
 * 设备配置控制命令
 */
public class DeviceConfigCmd extends AbstractControlCmd<DeviceConfigControlParams> {

    public DeviceConfigCmd(GB28181Properties properties) {
        super(properties);
    }

    @Override
    protected String buildCmdXML(DeviceConfigControlParams params) {
        final StringBuilder cmd = new StringBuilder(200);
        cmd.append("<BasicParam>");
        if (params.getDeviceName() != null && !params.getDeviceName().isEmpty()) {
            cmd.append("<Name>").append(params.getDeviceName()).append("</Name>");
        }
        if (params.getExpiration() != null) {
            cmd.append("<Expiration>").append(params.getExpiration()).append("</Expiration>");
        }
        if (params.getHeartBeatInterval() != null) {
            cmd.append("<HeartBeatInterval>").append(params.getHeartBeatInterval()).append("</HeartBeatInterval>");
        }
        if (params.getHeartBeatCount() != null) {
            cmd.append("<HeartBeatCount>").append(params.getHeartBeatCount()).append("</HeartBeatCount>");
        }
        cmd.append("</BasicParam>");

        return cmd.toString();
    }

    @Override
    protected void cacheFlowContext(DeviceConfigControlParams deviceConfigControlParams, Request request) {
        final FlowContext flowContext = new FlowContext(Operation.DEVICE_CONFIG, deviceConfigControlParams, new DefaultMessageResponseProcessor());
        FlowContextCacheUtil.put(getCallId(request), flowContext);
    }
}
