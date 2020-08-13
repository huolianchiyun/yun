package com.zhangbin.yun.yunrights.mapper;


import cn.hutool.core.collection.CollectionUtil;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectByUserName() {
        UserDO userDo = userMapper.selectByUserName("zhangsan");
        System.out.println(userDo.getUserName());
        System.out.println("----------------------------");
        System.out.println(userDo.getNickName());
        System.out.println("----------------------------");
        System.out.println(userDo.getGroups());
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
        List<UserDO> userDOS = CollectionUtil.list(false, userMapper.selectAllByCriteria(criteria));
        System.out.println("----------------------------");
        System.out.println(userDOS);
    }

    @Test
    public void testInsert(){
        UserDO user = new UserDO();
        user.setUserName("lisi");
        user.setNickName("李四");
        user.setPhone("12345678991");
        user.setPwd("12345678991");
        userMapper.insert(user);
        System.out.println("user:" + user.toString());

    }

}
