package com.zhangbin.yun.yunrights.modules.common.utils;

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

//    /**
//     * Page 数据处理，预防redis反序列化报错
//     */
//    public static Map<String, Object> toPage(Page page) {
//        Map<String, Object> map = new LinkedHashMap<>(2);
//        map.put("content", page.getContent());
//        map.put("total", page.getTotalElements());
//        return map;
//    }
//
//    /**
//     * 自定义分页
//     */
//    public static Map<String, Object> toPage(Object content, Object total) {
//        Map<String, Object> map = new LinkedHashMap<>(2);
//        map.put("content", content);
//        map.put("total", total);
//        return map;
//    }

}
