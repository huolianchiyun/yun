package com.zhangbin.yun.yunrights.modules.rights.datarights;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SpringContextHolder;
import com.zhangbin.yun.yunrights.modules.rights.mapper.GroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.PermissionRuleMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class RuleManager {
    // 声明该变量的目的在于规则更新那段时间中，当前线程暂时不受影响
    private final static ThreadLocal<Map<String, Set<PermissionRuleDO>>> ruleMapThreadLocal = new ThreadLocal<>();
    private static volatile Map<String, Set<PermissionRuleDO>> groupCodePermissionMap;

    public static Set<PermissionRuleDO> getRulesForCurrentUser() {
        if (groupCodePermissionMap == null) {
            init();
        } else {
            ruleMapThreadLocal.set(groupCodePermissionMap);
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
     * 移除本地变量，使用者可以不手动清除，程序会统一清除
     * {@link com.zhangbin.yun.yunrights.modules.common.config.filter.ThreadLocalClearFilter#doFilter}
     */
    public static void clearRuleMap() {
        ruleMapThreadLocal.remove();
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

    /**
     * 获取当前用户组及其父组的规则
     */
    private static Set<PermissionRuleDO> filterByCurrentUserGroups() {
        String currentUsername = SecurityUtils.getCurrentUsername();
        if ("anonymousUser".equalsIgnoreCase(currentUsername)) {
            return new HashSet<>(0);
        }
        Set<PermissionRuleDO> filtered = new HashSet<>(10);
        GroupService groupService = SpringContextHolder.getBean(GroupService.class);
        Set<String> groupCodes = groupService.queryGroupCodeByUsername(currentUsername);
        if (CollectionUtil.isNotEmpty(groupCodes)) {
            Map<String, Set<PermissionRuleDO>> ruleMap = ruleMapThreadLocal.get();
            if (CollectionUtil.isNotEmpty(ruleMap)) {
                Set<String> lineageSet = new HashSet<>();
                groupCodes.forEach(e -> lineageSet.addAll(getDirectLineage(e)));
                ruleMap.forEach((k, v) -> {
                    if (lineageSet.contains(k)) {
                        filtered.addAll(v);
                    }
                });
            }
        }
        return filtered;
    }

    private static Set<String> getDirectLineage(String groupCode) {
        if (!StringUtils.hasText(groupCode)) return new HashSet<String>(0);
        String[] codes = groupCode.split(":{1,2}");
        if (codes.length <= 2) {
            return CollectionUtil.newHashSet(groupCode);
        }
        String code = codes[0] + ":";
        HashSet<String> collect = new HashSet<>(codes.length - 1);
        for (int i = 1; i < codes.length; i++) {
            code += ":" + codes[i];
            collect.add(code);
        }
        return collect;
    }
}
