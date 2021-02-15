package com.hlcy.yun.gb28181.sip.message.handler;

import javax.sip.message.Message;

public interface MessageContextManager {
    MessageContext get(Message message);
}
