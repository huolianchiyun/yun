package com.zhangbin.yun.yunrights.sql;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.yun.sys.modules.rights.model.$do.PermissionRuleDO;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DruidSQLParserTest {

    @Test
    public void testModifySql() {
//        Assert.assertEquals("SELECT *" //
//                + "\nFROM t" //
//                + "\nWHERE id = 0", SQLUtils.addCondition("select * from t", "id = 0", null));
        System.out.println(SQLUtils.addCondition("select * from (select * from user where age > 30)where 1=1 group by create_time", "ss", null));

        Assert.assertEquals("SELECT *" //
                + "\nFROM t" //
                + "\nWHERE id = 0" //
                + "\n\tAND name = 'aaa'", SQLUtils.addCondition("select * from t where id = 0", "name = 'aaa'", null));
    }

    public void testModifySql1() {
        PermissionRuleDO ruleDO = new PermissionRuleDO();
        ruleDO.setRuleExps("age > 30");
        PermissionRuleDO ruleDO1 = new PermissionRuleDO();
        ruleDO1.setRuleExps("address = 'shanghai'");
        HashSet<PermissionRuleDO> set = CollectionUtil.newHashSet(ruleDO, ruleDO1);
        String condition = String.join(" and ", set.stream().map(PermissionRuleDO::getRuleExps).collect(Collectors.toSet()));
        System.out.println(condition);
        System.out.println(SQLUtils.addCondition("select * from (select * from user where age > 30)where 1=1 group by create_time", condition, null));
    }
}
