package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.sys.modules.logging.mapper.LogMapper;
import com.zhangbin.yun.sys.modules.logging.model.$do.LogDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogMapperTest {

    @Autowired
    LogMapper logMapper;

    @Test
    public void testInsert(){
        LogDO logDO = new LogDO();
        logDO.setBrowser("xxxxxxxx");
        logMapper.insert(logDO);
        System.out.println("log:" + logDO.toString());

    }

}
