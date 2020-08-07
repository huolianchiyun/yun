package com.zhangbin.yun.yunrights.modules.rights.common.excel;

import java.util.LinkedHashMap;

/**
 * 导出excel支持，pojo导出，需实现该接口
 */
@FunctionalInterface
public interface ExcelSupport {
    /**
     * Demo: {@link com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO}
     */
     LinkedHashMap<String, Object> toLinkedMap();
}
