package com.zhangbin.yun.yunrights.modules.rights.model.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zhangbin.yun.yunrights.modules.common.page.AbstractQueryPage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单公共查询类
 */
@Data
public class MenuQueryCriteria extends AbstractQueryPage {

    // 模块搜索条件
    private String blurry;

    private BlurryType blurryType;

    private List<LocalDateTime> createTimes;

    private Long pid;

    public MenuQueryCriteria setPid(Long pid) {
        this.pid = pid;
        return this;
    }

    public enum BlurryType {
        MENU_TITLE(1, "menuTitle"),
        DESCRIPTION(2, "description");

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
        public boolean isMatch(@NotNull String name) {
            return name.equals(this.name);
        }
    }
}
