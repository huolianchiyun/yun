package com.zhangbin.yun.yunrights.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SpringContextHolder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.GroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.PermissionRuleMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import java.util.*;
import java.util.regex.Pattern;

public final class RuleManager {
    private final static ThreadLocal<Map<String, Set<PermissionRuleDO>>> ruleMapThreadLocal = new ThreadLocal<>();
    private static volatile Map<String, Set<PermissionRuleDO>> groupCodePermissionMap;

    public static Set<PermissionRuleDO> getRulesForCurrentUser() {
        if (groupCodePermissionMap == null) {
            init();
        }
        return filterByCurrentUserGroups();
    }

    /**
     * 更新规则后，需调用该方法刷新缓存
     */
    public static void refreshCache() {
        synchronized (RuleManager.class) {
            groupCodePermissionMap.clear();
            groupCodePermissionMap = null;
            init();
        }
    }

    /**
     * 数据库加载所有的数据权限规则，并将其按照 key：groupCode --> value: Set<PermissionRuleDO>封装成 Map
     */
    private static void init() {
        synchronized (RuleManager.class) {
            if (groupCodePermissionMap == null) {
                Map<String, Set<PermissionRuleDO>> groupCodePermissionMap = new HashMap<>();
                getRules().stream().map(e -> {
                    String groupCodesStr = e.getGroupCodes();
                    HashMap<String, PermissionRuleDO> map = new HashMap<>(10);
                    if (Pattern.matches(".*[,].*", groupCodesStr)) {
                        String[] groupCodes = groupCodesStr.split(",");
                        for (String groupCode : groupCodes) {
                            map.put(groupCode, e);
                        }
                    }
                    return map;
                }).forEach(map -> {
                    map.forEach((k, v) -> {
                        if (groupCodePermissionMap.containsKey(k)) {
                            groupCodePermissionMap.get(k).add(v);
                        } else {
                            groupCodePermissionMap.put(k, CollectionUtil.newHashSet(v));
                        }
                    });
                });
                RuleManager.groupCodePermissionMap = groupCodePermissionMap;
                ruleMapThreadLocal.set(RuleManager.groupCodePermissionMap);
            }
        }
    }

    private static Set<PermissionRuleDO> getRules() {
        PermissionRuleMapper ruleMapper = SpringContextHolder.getBean(PermissionRuleMapper.class);
        return ruleMapper.selectAllUsableForSystem();
    }

    private static Set<PermissionRuleDO> filterByCurrentUserGroups() {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Set<PermissionRuleDO> filtered = new HashSet<>(10);
        if ("anonymousUser".equalsIgnoreCase(currentUsername)) {
            return filtered;
        }
        Set<String> groupCodes = SpringContextHolder.getBean(GroupMapper.class).selectByUsername(currentUsername);
        if (CollectionUtil.isNotEmpty(groupCodes)) {
            Map<String, Set<PermissionRuleDO>> ruleMap = ruleMapThreadLocal.get();
            if (CollectionUtil.isNotEmpty(ruleMap)) {
                ruleMap.forEach((k, v) -> {
                    if (groupCodes.contains(k)) {
                        filtered.addAll(v);
                    }
                });
            }
        }
        return filtered;
    }
}
