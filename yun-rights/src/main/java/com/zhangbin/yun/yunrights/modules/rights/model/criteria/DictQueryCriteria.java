package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDO;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import com.zhangbin.yun.yunrights.modules.rights.model.validation.ValidateBlurry;
import com.zhangbin.yun.yunrights.modules.rights.model.validation.ValidateDateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典公共查询类
 */
@Data
@ValidateBlurry(message = "请选择搜索类型！")
@ValidateDateRange(message = "开始时间不能大于结束时间！")
public class DictQueryCriteria extends AbstractQueryPage implements Serializable {

    @NotNull(groups = {BaseDO.Update.class}, message = "id 不能为空！")
    private String blurry;

    private BlurryType blurryType;

    private String dictTypeCode;

    private Integer dictStatus;
    /**
     * 搜索范围：开始时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-22")
    private String startTime;

    /**
     * 搜索范围：结束时间 （创建时间）
     */
    @ApiModelProperty("格式：2020-08-25")
    private String endTime;

    public enum BlurryType {
        DICT_NAME(1, "dict_name"),
        DICT_CODE(2, "dict_code");

        @JsonValue
        private int code;
        private String name;

        BlurryType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        @JsonCreator
        public BlurryType build(int code) {
            BlurryType[] values = values();
            for (BlurryType type : values) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 检测枚举 name是否与指定的name匹配
         *
         * @param name
         * @return
         */
        public boolean isMatch(String name) {
            return name.equals(this.name);
        }
    }
}
