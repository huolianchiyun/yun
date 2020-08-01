package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import java.io.Serializable;
import lombok.Data;

/**
 * 表 t_sys_group
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Data
public class GroupDo extends BaseDo implements Serializable {
    /**
     * 父组id
     */
    private Long pid;

    /**
     * 组类型
     */
    private Integer groupType;

    /**
     */
    private String groupName;

    /**
     */
    private String description;

    private static final long serialVersionUID = 1L;
}
