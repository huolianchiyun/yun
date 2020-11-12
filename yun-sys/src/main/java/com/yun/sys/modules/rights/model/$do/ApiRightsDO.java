package com.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.yun.common.model.BaseDO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * api 访问权限
 * 表 t_sys_api_rights
 * @author ASUS
 * @date 2020-09-29 09:32:27
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiRightsDO  extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    public ApiRightsDO(String group, String tag, String url, String authorization, String description, LocalDateTime creatTime) {
        this.group = group;
        this.tag = tag;
        this.url = url;
        this.authorization = authorization;
        this.description = description;
        this.createTime = creatTime;
    }

    /**
     * module group
     */
    private String group;

    /**
     * controller tag
     */
    private String tag;

    /**
     * request url
     */
    private String url;

    /**
     * access authorization of api url
     */
    private String authorization;

    /**
     * api description
     */
    private String description;

}
