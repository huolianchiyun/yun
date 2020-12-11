package com.hlcy.yun.sys.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hlcy.yun.common.mybatis.page.AbstractQueryPage;
import com.hlcy.yun.sys.modules.rights.model.validation.ValidateBlurry;
import com.hlcy.yun.sys.modules.rights.model.validation.ValidateDateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 数据权限规则公共查询类
 */
@Data
@ValidateBlurry(message = "请选择搜索类型！")
@ValidateDateRange(message = "开始时间不能大于结束时间！")
public class RuleQueryCriteria extends AbstractQueryPage implements Serializable {

    private String blurry;

    private BlurryType blurryType;


    private Boolean enabled;

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
        RULE_NAME(1, "ruleName"),
        GROUP_CODE(2, "groupCode");

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
