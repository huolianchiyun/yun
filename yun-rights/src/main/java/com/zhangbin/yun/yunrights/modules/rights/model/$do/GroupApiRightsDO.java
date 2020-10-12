package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 组和API权限关联表
 * 表 t_sys_group_api_rights
 * @author ASUS
 * @date 2020-10-12 21:25:54
 */
@Getter
@Setter
@NoArgsConstructor
public class GroupApiRightsDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 组ID
     */
    private Long groupId;

    /**
     * ApiRightsId
     */
    private String apiUrl;

    public GroupApiRightsDO(Long groupId, String apiUrl) {
        this.groupId = groupId;
        this.apiUrl = apiUrl;
    }
}
