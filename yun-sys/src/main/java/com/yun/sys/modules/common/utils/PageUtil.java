package com.yun.sys.modules.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static <T> List<T> toPage(int page, int size, List<T> list) {
        page = Math.max(page - 1, 0);
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if (fromIndex > list.size()) {
            return new ArrayList<>();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }
}
