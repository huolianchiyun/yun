package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import java.util.LinkedHashMap;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import com.zhangbin.yun.yunrights.modules.rights.model.common.NameValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典表
 * 表 t_sys_dictionary
 * @author ASUS
 * @date 2020-09-08 13:53:40
 */
@Getter
@Setter
public class DictDO extends BaseDo implements  Comparable<DictDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 字典类型编码
     */
    private String typeCode;

    /**
     * 字典类型显示名
     * --非表字段--
     */
    @ApiModelProperty(hidden = true)
    private Long typeName;

    /**
     * 显示名
     */
    private String name;

    /**
     * 字典值
     */
    private String code;

    /**
     * 排序码
     */
    private Integer sort;

    private String description;

    private Boolean status;


    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("显示名", name);
        map.put("字典编码", code);
        map.put("字典类型", typeName);
        map.put("排序码", sort);
        map.put("描述", description);
        map.put("创建人", creator);
        map.put("创建时间", createTime);
        map.put("修改人", updater);
        map.put("修改时间", updateTime);
        return map;
    }

    @Override
    public int compareTo(DictDO o) {
        return Integer.compare(sort == null ? 0 : sort, o.sort == null ? 0 : o.sort);
    }

    public NameValue<String> toNameValue(){
        return new NameValue<>(name, code);
    }
}
