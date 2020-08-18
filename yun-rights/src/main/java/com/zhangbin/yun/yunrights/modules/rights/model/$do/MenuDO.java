package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import com.zhangbin.yun.yunrights.modules.common.enums.handler.BaseEnumValue;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import lombok.Getter;
import lombok.Setter;

/**
 * 表 t_sys_menu
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
public class MenuDO extends BaseDo implements Comparable<MenuDO>, CollectChildren.ChildrenSupport<MenuDO>, ExcelSupport,Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String menuTitle;

    /**
     * 菜单编码，全局唯一
     */
    private String menuCode;

    /**
     * 菜单类型
     */
    private MenuType menuType;

    /**
     *
     */
    private String description;

    /**
     * 父菜单id
     */
    private Long pid;

    /**
     * Spring Security使用Spring EL表达式对URL或方法进行权限控制
     */
    private String permission;

    /**
     * 访问后端服务url
     */
    private String accessUrl;

    /**
     * 是否是外部链接
     */
    private Boolean externalLink;

    private Boolean hidden;

    private Integer menuSort;

    /**
     * 子菜单
     */
    private List<MenuDO> children;

    /**
     * 非表字段
     */
    @JsonIgnore
    private Long oldPid;

    @Transient
    public Long getOldPid() {
        return oldPid;
    }

    public Long getPid() {
        if(null == pid){
            pid = 0L;
        }
        return pid;
    }

    @Override
    public int compareTo(MenuDO o) {
        if (menuSort == null && o.menuSort == null) {
            return 0;
        } else if (menuSort != null && o.menuSort == null) {
            return 1;
        } else if (menuSort == null && o.menuSort != null) {
            return -1;
        } else {
            return Integer.compare(menuSort, o.menuSort);
        }
    }

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("菜单标题", menuTitle);
        map.put("菜单类型", menuType.getZhName());
        map.put("权限标识", permission);
        map.put("创建日期", createTime);
        return map;
    }

    public enum MenuType implements BaseEnumValue<Integer> {
        Dir(1, "directory", "目录"),
        MENU(2, "menu", "菜单"),
        BUTTON(3, "button", "按钮");

        @JsonValue
        private Integer code;
        private String enName;  // 英文名
        private String zhName;  // 中文名

        MenuType(int code, String enName, String zhName) {
            this.code = code;
            this.enName = enName;
            this.zhName = zhName;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getZhName() {
            return zhName;
        }

        public void setZhName(String zhName) {
            this.zhName = zhName;
        }

        @JsonCreator
        public MenuType build(int code) {
            MenuType[] values = values();
            for (MenuType type : values) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public Integer getValue() {
            return code;
        }
    }
}
