package com.hlcy.yun.sys.modules.rights.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public
class ButtonVO {
    private String url;
    private String method;

    public ButtonVO(String method, String url) {
        this.method = method;
        this.url = url;
    }
}
