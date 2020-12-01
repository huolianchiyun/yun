package com.yun.sys.modules.rights.common.tree;

import cn.hutool.core.collection.CollectionUtil;
import com.yun.common.model.BaseDO;
import com.yun.sys.modules.rights.common.excel.CollectChildren;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class TreeBuilder<T extends BaseDO & Comparable<T> & CollectChildren.ChildrenSupport<T>> {

    /**
     * 构建树结构
     * 父节点 ID 为 null 或 0 的节点为 树根节点
     *
     * @param sources 树节点源数据
     * @return {@link List<T>}
     */
    public List<T> buildTree(Collection<T> sources) {
        Map<Long, T> map = convertSourcesToMap(sources);
        sources.forEach(e -> {
            T father = map.getOrDefault(e.getPid(), null);
            if (Objects.nonNull(father)) {
                if (CollectionUtil.isEmpty(father.getChildren())) {
                    father.setChildren(new ArrayList<>());
                }
                father.getChildren().add(e);
            }
        });
        return sources.stream().peek(e -> {
            if (!CollectionUtils.isEmpty(e.getChildren())) {
                e.getChildren().sort(T::compareTo);
            }
        }).filter(e -> e.getPid() == null || e.getPid().equals(0L)).collect(Collectors.toList());
    }

    /**
     * 以指定的树根节点构建树结构
     * 若 sources 为空或 null，则返回空集合；
     * 若 treeRootIds 为空或 null，则返回{@link TreeBuilder#buildTree(java.util.Collection)}
     *
     * @param sources     树节点源数据
     * @param treeRootIds 指定的树根节点 ID
     * @return {@link List<T>}
     */
    public List<T> buildTree(Collection<T> sources, Set<Long> treeRootIds) {
        if (CollectionUtil.isEmpty(sources)) {
            return new ArrayList<>();
        }
        if (CollectionUtil.isEmpty(treeRootIds)) {
            return buildTree(sources);
        }
        Map<Long, T> map = convertSourcesToMap(sources);
        return sources.stream().peek(e -> {
            T father = map.getOrDefault(e.getPid(), null);
            if (Objects.nonNull(father)) {
                if (CollectionUtil.isEmpty(father.getChildren())) {
                    father.setChildren(new ArrayList<>(5));
                }
                father.getChildren().add(e);
            }
        }).filter(e -> treeRootIds.contains(e.getId())).collect(Collectors.toList());
    }

    private Map<Long, T> convertSourcesToMap(Collection<T> sources) {
        return sources.stream().collect(Collectors.toMap(T::getId, e -> e, (oldValue, newValue) -> newValue));
    }
}
