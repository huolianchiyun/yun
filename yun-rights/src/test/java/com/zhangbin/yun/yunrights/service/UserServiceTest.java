package com.zhangbin.yun.yunrights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    public void testInsert(){
        UserDO user = new UserDO();
        user.setUsername("lisisi");
        user.setNickname("李四四");
        user.setPhone("12345678991");
        user.setPwd("12345678991");
        userService.create(user);
        System.out.println("user:" + user.toString());
    }

}
