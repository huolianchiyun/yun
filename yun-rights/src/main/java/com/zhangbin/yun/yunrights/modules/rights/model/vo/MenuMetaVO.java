package com.zhangbin.yun.yunrights.modules.rights.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MenuMetaVO implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}
