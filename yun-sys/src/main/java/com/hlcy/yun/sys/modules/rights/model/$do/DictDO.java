package com.hlcy.yun.sys.modules.rights.model.$do;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.hlcy.yun.sys.modules.rights.model.common.NameValue;
import com.hlcy.yun.common.model.BaseDO;
import com.hlcy.yun.common.utils.date.DateUtil;
import com.hlcy.yun.common.utils.str.StringUtils;
import com.hlcy.yun.common.utils.download.excel.ExcelSupport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants.DICT_SUFFIX;

/**
 * 系统字典表
 * 表 t_sys_dictionary
 *
 * @author ASUS
 * @date 2020-09-08 13:53:40
 */
@Getter
@Setter
@NoArgsConstructor
public class DictDO extends BaseDO implements Comparable<DictDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型编码
     */
    @NotBlank(groups = {Create.class}, message = "typeCode 不能为空！")
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
    @NotBlank(groups = {Create.class}, message = "name 不能为空！")
    private String name;

    /**
     * 字典值
     */
    @ApiModelProperty(required = true)
    @NotBlank(groups = {Create.class}, message = "code 不能为空！")
    private String code;

    public DictDO(String code) {
        this.code = code;
    }

    public DictDO(Long id, String code) {
        this.id = id;
        this.code = code;
    }


    public void setCode(String code) {
        if (StringUtils.isNotBlank(code) && !code.endsWith(DICT_SUFFIX)) {
            code += DICT_SUFFIX;
        }
        this.code = code;
    }

    /**
     * 排序码
     */
    private int sort = 0;

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
        map.put("创建时间", DateUtil.format2MdHms(createTime));
        map.put("修改人", updater);
        map.put("修改时间", DateUtil.format2MdHms(updateTime));
        return map;
    }

    @Override
    public int compareTo(DictDO o) {
        return Integer.compare(sort, o.sort);
    }

    public NameValue<String> toNameValue() {
        return new NameValue<>(name, code);
    }
}
