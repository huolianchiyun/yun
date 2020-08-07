package com.zhangbin.yun.yunrights.modules.rights.common.tree;

import com.zhangbin.yun.yunrights.modules.common.model.$do.BaseDo;
import com.zhangbin.yun.yunrights.modules.rights.common.excel.CollectChildren;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class TreeBuilder<T extends BaseDo & Comparable<T> & CollectChildren.ChildrenSupport<T>> {

    private TreeBuilder() {
    }

    public static TreeBuilder build() {
        return new TreeBuilder();
    }

    public List<T> buildTree(Collection<T> sources) {
        Map<Long, T> map = sources.stream().collect(Collectors.toMap(T::getId, e -> e, (oldValue, newValue) -> newValue));
        sources.forEach(e -> {
            T father = map.getOrDefault(e.getPid(), null);
            if (Objects.nonNull(father)) {
                father.getChildren().add(e);
            }
        });
        return sources.stream().peek(e -> {
            if (!CollectionUtils.isEmpty(e.getChildren())) {
                e.getChildren().sort(T::compareTo);
            }
        }).filter(e -> e.getPid() == null || e.getPid().equals(0L)).collect(Collectors.toList());
    }
}
