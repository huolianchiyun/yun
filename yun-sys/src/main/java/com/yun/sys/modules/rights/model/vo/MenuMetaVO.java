package com.yun.sys.modules.rights.model.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class MenuMetaVO implements Serializable {
    private String title;

    private String icon;

    private Boolean noCache;

    private String method;

    private String url;

    private Boolean externalLink;

    private String externalLinkUrl;

    public MenuMetaVO(String title, String icon, String method, String url, Boolean externalLink, String externalLinkUrl) {
        this.title = title;
        this.icon = icon;
        this.method = method;
        this.url = url;
        this.externalLink = externalLink;
        this.externalLinkUrl = externalLinkUrl;
    }
}
