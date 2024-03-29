package com.hlcy.yun.common.utils.collect;


import java.util.*;

public final class SetUtils {

    public static <T> List<T> toListWithSorted(Set<T> set, Comparator<? super T> c) {
        List<T> list = new ArrayList<>(Optional.ofNullable(set).orElseGet(HashSet::new));
        Collections.sort(list, c);
        return list;
    }
}
