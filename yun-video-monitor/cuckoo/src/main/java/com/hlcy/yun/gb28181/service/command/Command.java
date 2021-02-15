package com.hlcy.yun.gb28181.service.command;

public interface Command<T> {

    void execute(T t);
}
