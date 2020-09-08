package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.helper;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import com.zhangbin.yun.yunrights.modules.rights.datarights.dialect.AbstractDialect;
import org.apache.ibatis.mapping.BoundSql;

import java.util.Map;
import java.util.Set;

public class MySqlDialect extends AbstractDialect {

    @Override
    public String getPermissionSqlForSelect(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        // TODO mysql 数据权限处理逻辑  根据权限修改原有sql --难点在于解析sql
        String originalSql = boundSql.getSql();
        if(CollectionUtil.isEmpty(rules)){
            return originalSql;
        }
        Map<String, Set<PermissionRuleDO>> toTableRuleMap = toTableRuleMap(rules);
        StringBuilder rightsSql = new StringBuilder();
        // 获取sql中的表名





        // 根据表名应用相应的rule

        return rightsSql.toString();
    }


    @Override
    public String getPermissionSqlForUpdate(BoundSql boundSql, Set<PermissionRuleDO> rules) {
        //获取到原始sql语句
        String sql = boundSql.getSql();
        /*仅有创建者才能修改成功*/
        if (sql.toLowerCase().contains("where")) {
            String prefix = findColumnPrefix(sql);
            sql += " and " + prefix + "_creator = '" + SecurityUtils.getCurrentUsername() + "'";
        }
        return sql;
    }
}
