package com.hlcy.yun.sys.service;

import com.hlcy.yun.sys.YunRightsApplication;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest(classes = YunRightsApplication.class)
public class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    @WithMockUser("admin")
    public void testInsert(){
        UserDO user = new UserDO();
        user.setUsername("lisisi33");
        user.setNickname("李四四");
        user.setPhone("12345678991");
        user.setPwd("123456");
        userService.create(user);
        System.out.println("user:" + user.toString());
    }
}
