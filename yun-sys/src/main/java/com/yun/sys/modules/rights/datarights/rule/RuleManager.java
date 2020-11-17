package com.yun.sys.modules.rights.datarights.rule;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.handler.BeanListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.yun.sys.modules.common.config.RightsBeanPostProcessor;
import com.yun.sys.modules.common.filter.ThreadLocalClearFilter;
import com.yun.common.spring.redis.RedisUtils;
import com.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Pattern;

import static com.yun.sys.modules.common.xcache.CacheKey.BIND_USER;
import static com.yun.sys.modules.common.xcache.CacheKey.RULE;
import static com.yun.sys.modules.rights.common.constant.RightsConstants.ANONYMOUS_USER;

/**
 * 数据权限规则管理类
 * Assign a value to variable dataSource and redisUtils
 *
 * @See {@link RightsBeanPostProcessor}
 */
@Slf4j
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = RULE)
public class RuleManager {
    // 声明该变量的目的在于规则更新那段时间中，当前线程暂时不受影响
    private final static ThreadLocal<Map<String, Set<PermissionRuleDO>>> ruleMapThreadLocal = new ThreadLocal<>();
    private static Map<String, Set<PermissionRuleDO>> groupCodePermissionMap;
    private static DataSource dataSource;
    private static RedisUtils redisUtils;

    @Cacheable(value = BIND_USER + RULE, key = "'rule:username:' + #p0")
    public Set<PermissionRuleDO> getRulesForCurrentUser(String currentUsername) {
        ruleMapThreadLocal.set(groupCodePermissionMap);
        return filterByCurrentUserGroups(currentUsername);
    }

    /**
     * 更新规则后，需调用该方法刷新缓存
     */
    public static void refreshCache() {
        synchronized (RuleManager.class) {
            groupCodePermissionMap.clear();
            groupCodePermissionMap = null;
            init();
            // 清理缓存
            redisUtils.delKeysWithSomePrefixByLua("rule::*");
        }
    }

    /**
     * 移除本地变量，使用者可以不手动清除，程序会统一清除
     * {@link ThreadLocalClearFilter#doFilter}
     */
    public static void clearRuleMap() {
        ruleMapThreadLocal.remove();
    }

    /**
     * 数据库加载所有的数据权限规则，并将其按照 key：groupCode --> value: Set<PermissionRuleDO>形式封装成 Map
     */
    @PostConstruct
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
        // 为了避免不必要的递归拦截造成堆溢出或栈溢出，此处采用原始jdbc获取数据权限规则
        String sql = "select pr_rule_name, pr_group_codes, pr_from_table, pr_rule_exps from t_sys_permission_rule where pr_is_enabled = true";
        return query(sql, new HashMap<>(0), PermissionRuleDO.class, "数据权限规则加载失败！！！");
    }

    /**
     * 获取当前用户组及其父组的规则
     */
    private Set<PermissionRuleDO> filterByCurrentUserGroups(String currentUsername) {
        if (ANONYMOUS_USER.equalsIgnoreCase(currentUsername)) {
            return new HashSet<>(0);
        }
        String sql = "select g_group_code from t_sys_group where g_id in (select group_id from t_sys_user_group join t_sys_user on user_id = u_id and u_username = :username)";
        HashMap<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("username", currentUsername);
        Set<String> groupCodes = query(sql, paramMap, String.class, String.format("数据权限处理，%s : group code 加载失败", currentUsername));
        Set<PermissionRuleDO> filtered = new HashSet<>(10);
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

    private static <T> Set<T> query(String sql, Map<String, Object> paramMap, Class<T> resultType, String errorMsg) {
        try (Connection conn = dataSource.getConnection()) {
            return new HashSet<>(SqlExecutor.query(conn, sql, new BeanListHandler<>(resultType), paramMap));
        } catch (Exception e) {
            log.error(errorMsg, e);
            e.printStackTrace();
        }
        return new HashSet<>(0);
    }
}
