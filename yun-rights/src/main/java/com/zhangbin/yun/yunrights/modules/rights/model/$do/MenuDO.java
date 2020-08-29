package com.zhangbin.yun.yunrights.modules.rights.model.$do;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.beans.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.zhangbin.yun.yunrights.modules.common.enums.handler.BaseEnumValue;
import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.ExcelSupport;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.MenuMetaVO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.MenuVO;
import io.swagger.annotations.ApiModelProperty;
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
public class MenuDO extends BaseDo implements Comparable<MenuDO>, CollectChildren.ChildrenSupport<MenuDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单标题，显示名
     */
    @ApiModelProperty(required = true)
    private String menuTitle;

    /**
     * 菜单编码，全局唯一
     */
    private String menuCode;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单类型
     */
    @ApiModelProperty(required = true)
    private MenuType menuType;

    /**
     * 菜单描述
     */
    private String description;

    /**
     * 父菜单id
     */
    @ApiModelProperty(required = true)
    private Long pid;

    /**
     * Spring Security使用Spring EL表达式对URL或方法进行权限控制
     */
    private String permission;

    /**
     * 菜单路由名称
     */
    private String routerName;

    /**
     * 菜单路由地址
     */
    private String routerPath;

    /**
     * 菜单组件路径
     */
    private String component;

    /**
     * 是否是外部链接
     */
    @ApiModelProperty(required = true)
    private Boolean externalLink;

    private Boolean hidden;

    private Integer menuSort;

    /**
     * 子菜单（非表字段）
     */
    @ApiModelProperty(hidden = true)
    private List<MenuDO> children;

    /**
     * 非表字段
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long oldPid;

    @Transient
    public Long getOldPid() {
        return oldPid;
    }

    public Long getPid() {
        if (null == pid) {
            pid = 0L;
        }
        return pid;
    }

    public MenuVO toMenuVO() {
        String menuComponent = StrUtil.isEmpty(component) ? "Layout" : component;
        MenuVO menuVO = new MenuVO(routerName, routerPath, menuComponent, hidden);
        menuVO.setMeta(new MenuMetaVO(menuTitle, menuIcon));
        if (CollectionUtil.isNotEmpty(children)) {
            menuVO.setRedirect("noredirect");
            menuVO.setChildren(children.stream().map(MenuDO::toMenuVO).collect(Collectors.toList()));
        }
        return menuVO;
    }

    @Override
    public int compareTo(MenuDO o) {
        return Integer.compare(menuSort == null ? 0 : menuSort, o.menuSort == null ? 0 : o.menuSort);
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
