package com.hlcy.yun.gb28181.sip.message;


public interface MessageInvoker<E> {
    void processMessage(E event);
}
