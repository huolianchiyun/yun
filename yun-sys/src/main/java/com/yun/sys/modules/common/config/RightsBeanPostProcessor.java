package com.yun.sys.modules.common.config;

import com.yun.sys.modules.common.xcache.RedisCache;
import com.yun.sys.modules.rights.datarights.DataRightsHelper;
import com.yun.sys.modules.rights.datarights.rule.RuleManager;
import com.yun.sys.modules.rights.datarights.util.StatementHandlerUtil;
import com.yun.sys.modules.rights.service.UserService;
import com.yun.common.spring.redis.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.lang.reflect.Field;

@Slf4j
@Component
public class RightsBeanPostProcessor implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {
    @SneakyThrows
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof DataSource) {
            setClassStaticField(RuleManager.class, "dataSource", bean);
        }
        if (bean instanceof RedisUtils) {
            setClassStaticField(RuleManager.class, "redisUtils", bean);
        }
        return bean;  // you can return any other object as well
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;  // you can return any other object as well
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Set RuleManager proxy for StatementHandlerUtil's  ruleManager field.
        RuleManager ruleManager = event.getApplicationContext().getBean(RuleManager.class);
        setClassStaticField(StatementHandlerUtil.class, "ruleManager", ruleManager);
        setClassStaticField(DataRightsHelper.class, "ruleManager", ruleManager);

        UserService userService = event.getApplicationContext().getBean(UserService.class);
        setClassStaticField(RedisCache.class, "userService", userService);
    }

    private void setClassStaticField(Class<?> forClass, String fieldName, Object fieldValue) {
        try {
            Field field = forClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("inject static field:<{}> for {}", fieldName, RuleManager.class.getName());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
