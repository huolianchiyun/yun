package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.yunrights.modules.rights.mapper.UserDoMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDoMapperTest {

    @Autowired
    UserDoMapper userDoMapper;


    @Test
    public void testSelectByUserName() {
        UserDo userDo = userDoMapper.selectByUserName("zhangsan");
        System.out.println(userDo);
    }

}
