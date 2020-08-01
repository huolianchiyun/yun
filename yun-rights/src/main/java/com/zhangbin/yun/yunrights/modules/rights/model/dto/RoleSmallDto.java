package com.zhangbin.yun.yunrights.modules.rights.model.dto;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleSmallDto extends BaseDo implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
