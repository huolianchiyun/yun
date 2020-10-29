package com.zhangbin.yun.sys.modules.rights.model.$do;

import com.zhangbin.yun.common.model.BaseDO;
import java.io.Serializable;

import com.zhangbin.yun.sys.modules.rights.model.common.NameValue;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型表
 * 表 t_sys_dict_type
 * @author ASUS
 * @date 2020-09-15 10:20:49
 */
@Getter
@Setter
public class DictTypeDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 显示名
     */
    private String name;

    /**
     * 类型编码
     */
    private String code;

    /**
     */
    private String description;

    /**
     */
    private int sort;

    public NameValue<String> toNameValue(){
        return new NameValue<>(name, code);
    }
}
