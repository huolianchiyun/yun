package com.hlcy.yun.common.utils.download.excel;

import java.util.LinkedHashMap;

/**
 * pojo excel导出支持，需实现该接口
 */
@FunctionalInterface
public interface ExcelSupport {
    /**
     * Demo: {@code com.zhangbin.yun.sys.modules.rights.model.$do.GroupDO}
     */
    LinkedHashMap<String, Object> toLinkedMap();
}
