package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户公共查询类
 */
@Data
public class UserQueryCriteria extends AbstractQueryPage implements Serializable {

    private Long id;

    private Set<Long> deptIds = new HashSet<>();

    private BlurryType blurryType;

    private String blurry;

    private Boolean status;

    private Long deptId;

    private List<LocalDateTime> createTimes;

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
