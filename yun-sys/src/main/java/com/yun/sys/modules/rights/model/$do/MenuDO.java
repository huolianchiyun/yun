package com.yun.sys.modules.rights.model.$do;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

import java.beans.Transient;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.yun.sys.modules.common.enums.handler.BaseEnumValue;
import com.yun.sys.modules.rights.common.excel.CollectChildren;
import com.yun.common.utils.date.DateUtil;
import com.yun.common.model.BaseDO;
import com.yun.common.utils.download.excel.ExcelSupport;
import com.yun.sys.modules.rights.model.vo.ButtonVO;
import com.yun.sys.modules.rights.model.vo.MenuMetaVO;
import com.yun.sys.modules.rights.model.vo.MenuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 表 t_sys_menu
 *
 * @author ASUS
 * @date 2020-07-29 23:10:43
 */
@Getter
@Setter
@JsonIgnoreProperties("handler")
public class MenuDO extends BaseDO implements Comparable<MenuDO>, CollectChildren.ChildrenSupport<MenuDO>, ExcelSupport, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单标题，显示名
     */
    @ApiModelProperty(required = true)
    @NotBlank(groups = Create.class, message = "menuTitle 不能为空！")
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
    @NotNull(groups = Create.class, message = "menuType 不能为空！")
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
     * 菜单组件路径
     */
    private String component;

    /**
     * 菜单路由名称
     */
    private String routerName;

    /**
     * 菜单路由地址
     */
    private String routerPath;

    /**
     * 是否是外部链接
     */
    private Boolean externalLink = false;

    /**
     * 外链接地址
     */
    private String externalLinkUrl;

    private Boolean hidden;

    private int menuSort;

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

    /**
     * 非表字段，菜单绑定 api rights时传绑定的 api url set
     */
    @ApiModelProperty(notes="菜单绑定 api rights set")
    private Set<String> apiUrls;

    /**
     * 非表字段， 级联查询菜单绑定的 api rights set
     */
    @ApiModelProperty(hidden = true)
    private Set<ApiRightsDO> apiRightsSet;

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
        final String[] api = getAPI();
        menuVO.setMeta(new MenuMetaVO(menuTitle, menuIcon, api[0], api[1], externalLink, externalLinkUrl));
        if (CollectionUtil.isNotEmpty(children)) {
            menuVO.setChildren(children.stream().map(MenuDO::toMenuVO).collect(Collectors.toList()));
        }
        return menuVO;
    }

    public ButtonVO toButtonVO() {
        final String[] api = getAPI();
        return new ButtonVO(api[0], api[1]);
    }

    private String[] getAPI() {
        String[] apis = new String[2];
        if (CollectionUtil.isNotEmpty(apiRightsSet)) {
            // apiUrl: {GET /yun/api/rights/group}
            StringBuilder methods = new StringBuilder();
            StringBuilder urls = new StringBuilder();
            apiRightsSet.forEach(api -> {
                        final String[] method_url = api.getUrl().replaceAll("[{}]", "").split("\\s+");
                        methods.append(method_url[0]).append(";");
                        urls.append(method_url[1]).append(";");
                    }
            );
            apis[0] = methods.deleteCharAt(methods.lastIndexOf(";")).toString();
            apis[1] = urls.deleteCharAt(urls.lastIndexOf(";")).toString();
        }
        return apis;
    }

    @Override
    public int compareTo(MenuDO o) {
        return Integer.compare(menuSort, o.menuSort);
    }

    @Override
    public LinkedHashMap<String, Object> toLinkedMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("菜单标题", menuTitle);
        map.put("菜单类型", menuType.getZhName());
        map.put("菜单编码", menuCode);
        map.put("菜单图标", menuIcon);
        map.put("创建人", creator);
        map.put("创建日期", DateUtil.format2MdHms(createTime));
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuDO)) return false;
        MenuDO menuDO = (MenuDO) o;
        return Objects.equals(id, menuDO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pid, menuCode);
    }

    public enum MenuType implements BaseEnumValue<Integer> {
        Dir(0, "directory", "目录"),
        MENU(1, "menu", "菜单"),
        BUTTON(2, "button", "按钮");

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
