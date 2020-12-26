package com.hlcy.yun.sip.gb28181.message;


public abstract class MessageHandler<E> {
    protected MessageHandler<E> prev;
    protected MessageHandler<E> next;

    public abstract void handle(E event);
}
