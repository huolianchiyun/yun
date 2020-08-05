package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.yunrights.modules.rights.mapper.RoleMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleMapperTest {

    @Autowired
    RoleMapper roleMapper;


    @Test
    public void testSelectByUserName() {
        RoleDO roleDo = roleMapper.selectByPrimaryKey(1L);
        System.out.println(roleDo.getRoleName());
        System.out.println("----------------------------");
        System.out.println(roleDo.getUsers());
    }

}
