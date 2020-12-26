package com.hlcy.yun.sip.gb28181.message;


public interface MessageInvoker<E> {
    void processMessage(E event);
}
