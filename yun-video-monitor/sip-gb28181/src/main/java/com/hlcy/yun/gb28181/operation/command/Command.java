package com.hlcy.yun.gb28181.operation.command;

public interface Command<T> {

    void execute(T t);
}
