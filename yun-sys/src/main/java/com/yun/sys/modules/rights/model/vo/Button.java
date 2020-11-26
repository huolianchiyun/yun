package com.yun.sys.modules.rights.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public
class Button {
    private String url;

    public Button(String url) {
        this.url = url;
    }
}
