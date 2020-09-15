package com.zhangbin.yun.yunrights.modules.rights.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameValue<T> {
    private String name;
    private T value;

    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
