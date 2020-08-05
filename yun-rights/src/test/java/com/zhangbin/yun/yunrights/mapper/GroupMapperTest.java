package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectByUserName() {
        UserDO userDo = userMapper.selectByUserName("zhangsan");
        System.out.println(userDo.getUserName());
        System.out.println("----------------------------");
        System.out.println(userDo.getNickName());
        System.out.println("----------------------------");
        System.out.println(userDo.getRoles());
        System.out.println("----------------------------");
        System.out.println(userDo.getDept());
    }

    @Test
    public void testSelectAllByCriteria() {
        UserQueryCriteria criteria = new UserQueryCriteria();
        criteria.setPageNum(1);
        criteria.setPageSize(5);
        criteria.setBlurryType(UserQueryCriteria.BlurryType.NICK_NAME);
        criteria.setBlurry("张三");
        List<UserDO> userDOS = userMapper.selectAllByCriteria(criteria);
        System.out.println("----------------------------");
        System.out.println(userDOS);
    }

}
