package com.hlcy.yun.sys.mapper;


import cn.hutool.core.collection.CollectionUtil;
import com.hlcy.yun.sys.YunRightsApplication;
import com.hlcy.yun.sys.modules.rights.mapper.UserMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.UserQueryCriteria;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
//@SpringBootTest(classes = YunRightsApplication.class)
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectByUsername() {
        UserDO userDo = userMapper.selectByUsername("zhangsan");
        System.out.println(userDo.getUsername());
        System.out.println("----------------------------");
        System.out.println(userDo.getNickname());
        System.out.println("----------------------------");
        System.out.println(userDo.getGroups());
        System.out.println("----------------------------");
        System.out.println(userDo.getDept());
    }

    @Test
    public void testSelectAllByCriteria() {
        UserQueryCriteria criteria = new UserQueryCriteria();
//        criteria.setPageNum(1);
//        criteria.setPageSize(5);
//        criteria.setBlurryType(UserQueryCriteria.BlurryType.NICKNAME);
//        criteria.setBlurry("张三");
        List<UserDO> userDOS = CollectionUtil.list(false, userMapper.selectByCriteria(criteria));
        System.out.println("----------------------------");
        System.out.println(userDOS);
    }

    @Test
    public void testInsert() {
        UserDO user = new UserDO();
        user.setUsername("lisi");
        user.setNickname("李四");
        user.setPhone("12345678991");
        user.setPwd("12345678991");

        userMapper.insert(user);
        System.out.println("user:" + user.toString());

    }

    @Test
    @WithMockUser(username = "test")
    public void testBatchInsert() {
        UserDO user1 = new UserDO();
        user1.setUsername("lisi666");
        user1.setNickname("李四666");
        user1.setPhone("12345678991");
        user1.setPwd("12345678991");

        UserDO user2 = new UserDO();
        user2.setUsername("lisi888");
        user2.setNickname("李四888");
        user2.setPhone("12345678991");
        user2.setPwd("12345678991");

        userMapper.batchInsert(new HashSet<>(Arrays.asList(user1, user2)));

    }

}
