package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.common.mybatis.page.AbstractQueryPage;
import com.zhangbin.yun.yunrights.modules.rights.model.validation.ValidateBlurry;
import com.zhangbin.yun.yunrights.modules.rights.model.validation.ValidateDateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户公共查询类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ValidateBlurry(message = "请选择搜索类型！")
@ValidateDateRange(message = "开始时间不能大于结束时间！")
public class UserQueryCriteria extends AbstractQueryPage implements Serializable {

    private String blurry;

    private BlurryType blurryType;

    private Boolean status;

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
        USERNAME(1, "username"),
        NICKNAME(2, "nickname"),
        EMAIL(3, "email"),
        PHONE(4, "phone");

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
