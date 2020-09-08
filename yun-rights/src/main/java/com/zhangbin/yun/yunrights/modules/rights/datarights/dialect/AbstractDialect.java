package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDialect implements Dialect {
    private final static Pattern pattern = Pattern.compile("(\\w+_\\w+)");

    /**
     * 该方法不会被调用
     * 查看 {@link com.zhangbin.yun.yunrights.modules.rights.datarights.DataRightsHelper#skip}
     *
     * @param ms MappedStatement
     * @return
     */
    @Override
    public boolean skip(MappedStatement ms) {
        return true;
    }

    @Override
    public boolean skipForUpdate(MappedStatement ms) {
        return true;
    }

    @Override
    public boolean beforeRightsQuery(MappedStatement ms) {
        return true;
    }

    @Override
    public String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        return boundSql.getSql();
    }

    @Override
    public String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        return boundSql.getSql();
    }

    @Override
    public void afterAll() {
    }

    @Override
    public void setProperties(Properties properties) {
    }

    protected static String findColumnPrefix(String sql) {
        String toLowerCaseSql = sql.toLowerCase();
        String replacedSql = toLowerCaseSql.substring(toLowerCaseSql.lastIndexOf("where")).replaceAll("[()?]", " ");
        Matcher matcher = pattern.matcher(replacedSql);
        if (matcher.find()) {
            String group = matcher.group(1);
            return group.substring(0, group.lastIndexOf('_'));
        }
        return "";
    }

    /**
     * 将规则转换为 Map<tableName : Set<PermissionRuleDO>>形式
     *
     * @param rules 待转规则集合
     * @return Map<String, Set < PermissionRuleDO>>
     */
    protected static Map<String, Set<PermissionRuleDO>> toTableRuleMap(Set<PermissionRuleDO> rules) {
        Map<String, Set<PermissionRuleDO>> map = new HashMap<>(rules.size());
        rules.forEach(rule -> {
            String fromTable = rule.getFromTable();
            if (map.containsKey(fromTable)) {
                map.get(fromTable).add(rule);
            } else {
                map.put(fromTable, CollectionUtil.newHashSet(rule));
            }
        });
        return map;
    }
}
