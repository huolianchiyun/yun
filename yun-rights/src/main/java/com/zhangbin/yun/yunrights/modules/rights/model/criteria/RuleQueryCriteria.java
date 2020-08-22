package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据权限规则公共查询类
 */
@Data
public class RuleQueryCriteria extends AbstractQueryPage implements Serializable {

    private BlurryType blurryType;

    private String blurry;

    private Boolean enabled;

    private List<LocalDateTime> createTimes;

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
