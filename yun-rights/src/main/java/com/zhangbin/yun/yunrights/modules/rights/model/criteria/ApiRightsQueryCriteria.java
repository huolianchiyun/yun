package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Getter
@Setter
public class ApiRightsQueryCriteria extends AbstractQueryPage implements Serializable {

    private String blurry;

    private BlurryType blurryType;

    public enum BlurryType {
        GROUP(1, "group"),
        TAG(2, "tag"),
        URL(3, "url"),
        DESCRIPTION(4, "description");

        @JsonValue
        private int code;
        private String name;

        BlurryType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        @JsonCreator
        public ApiRightsQueryCriteria.BlurryType build(int code) {
            ApiRightsQueryCriteria.BlurryType[] values = values();
            for (ApiRightsQueryCriteria.BlurryType type : values) {
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
        public boolean isMatch(@NotNull String name) {
            return name.equals(this.name);
        }
    }
}
