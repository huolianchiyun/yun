package com.zhangbin.yun.yunrights.modules.rights.common.excel;

import cn.hutool.core.collection.CollectionUtil;
import java.util.List;
import java.util.function.Consumer;

/**
 * 收集自己及其自己的子集，并按顺序收集
 * 伪示例效果：[{"a": a, "children":[a, a, a]}, {a: a, "children":[a, a, a]}}] => [a, a, a, a, a, a, a, a, a, a]
 */
public class CollectChildren<T extends CollectChildren.ChildrenSupport> implements Consumer<T> {

    private List<T> list;

    public CollectChildren(List<T> list) {
        this.list = list;
    }

    @Override
    public void accept(T t) {
        list.add(t);
        List<T> children = t.getChildren();
        t.setChildren(null);
        if (CollectionUtil.isNotEmpty(children)) {
            children.forEach(this);
        }
    }

    public interface ChildrenSupport<T>{
        List<T> getChildren();
        void setChildren(List<T> t);
    }
}
