package com.zhangbin.yun.yunrights.auto;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Objects;

public class MyBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(bean instanceof MybatisProperties){
            MybatisProperties  properties = (MybatisProperties) bean;
            String[] mapperLocations = properties.getMapperLocations();
            if (Objects.isNull(mapperLocations)) {
                properties.setConfigLocation("classpath:mybatis-config.xml");
                properties.setMapperLocations(new String[]{"classpath:mapper/**/*.xml"});
            }
        }
        return bean;  // you can return any other object as well
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;  // you can return any other object as well
    }

}
