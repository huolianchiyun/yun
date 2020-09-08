package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
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
public class DictionaryDO extends BaseDo implements  Comparable<DictionaryDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     */
    private Long id;

    /**
     * 字典编码，用于字典记录归类
     */
    private String code;

    /**
     * 显示名
     */
    private String name;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     */
    private String description;


    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("字典编码", code);
        map.put("显示名", name);
        map.put("字典值", value);
        map.put("排序码", sort);
        map.put("描述", description);
        map.put("创建人", creator);
        map.put("创建时间", createTime);
        map.put("修改人", updater);
        map.put("修改时间", updateTime);
        return map;
    }

    @Override
    public int compareTo(DictionaryDO o) {
        return Integer.compare(sort == null ? 0 : sort, o.sort == null ? 0 : o.sort);
    }
}
