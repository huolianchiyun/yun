package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签表
 * 表 t_vm_tag
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class Tag extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签值
     */
    private String value;

}
