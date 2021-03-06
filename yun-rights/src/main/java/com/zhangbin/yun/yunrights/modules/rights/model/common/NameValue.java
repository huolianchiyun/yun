package com.zhangbin.yun.yunrights.modules.rights.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NameValue<T> {
    private String name;
    private T value;

    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
