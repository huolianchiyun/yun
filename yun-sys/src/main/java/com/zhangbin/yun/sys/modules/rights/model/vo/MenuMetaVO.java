package com.zhangbin.yun.sys.modules.rights.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MenuMetaVO implements Serializable {
    private String title;

    private String icon;

    private Boolean noCache;

    public MenuMetaVO(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }
}
