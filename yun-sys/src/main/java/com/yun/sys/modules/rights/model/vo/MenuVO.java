package com.yun.sys.modules.rights.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVO implements Serializable {
    private String name;

    private String path;

    private Boolean hidden;

    private Boolean externalLink = false;

    private String redirect;

    private String component;

    private MenuMetaVO meta;

    private List<MenuVO> children;

    public MenuVO(String name, String path, String component, Boolean hidden, Boolean externalLink) {
        this.name = name;
        this.path = path;
        this.component = component;
        this.hidden = hidden;
        this.externalLink = externalLink;
    }
}
