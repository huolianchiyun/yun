package com.zhangbin.yun.yunrights.auto;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = YunRightsAutoConfig.SCAN_PACKAGE)
@MapperScan(value = YunRightsAutoConfig.SCAN_PACKAGE, annotationClass = Mapper.class)
public class YunRightsAutoConfig {
    static final String SCAN_PACKAGE = "com.zhangbin.yun.yunrights.modules";
}
