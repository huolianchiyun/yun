package com.hlcy.yun.gb28181.sip.biz;

import javax.sip.RequestEvent;

public interface RequestProcessorFactory {
    RequestProcessor getRequestProcessor(RequestEvent event);
}
