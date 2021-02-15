package com.hlcy.yun.gb28181.sip;

import com.hlcy.yun.gb28181.config.GB28181Properties;
import com.hlcy.yun.gb28181.service.listener.SipEventListenerManager;
import com.hlcy.yun.gb28181.sip.biz.RegisterProcessor;
import com.hlcy.yun.gb28181.sip.biz.RequestProcessorFactory;
import com.hlcy.yun.gb28181.sip.message.handler.RequestHandler;
import com.hlcy.yun.gb28181.sip.message.handler.request.RegisterRequestHandler;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class ServerBootstrap {
    private GB28181Properties properties;
    private RequestProcessorFactory requestProcessorFactory;
    private RegisterProcessor registerProcessor;
    private ServerInitializer initializer;

    public void start() {
        initializer.init();
        RequestHandler.setProcessorFactory(requestProcessorFactory);
        RegisterRequestHandler.setRegisterProcessor(registerProcessor);
        SipEventListenerManager.register();
        SipLayer.start(properties);
    }
}
