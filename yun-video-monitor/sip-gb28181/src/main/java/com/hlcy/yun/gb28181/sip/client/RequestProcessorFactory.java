package com.hlcy.yun.gb28181.sip.client;

import javax.sip.RequestEvent;

public interface RequestProcessorFactory {
    RequestProcessor getRequestProcessorBy(RequestEvent event);
}
