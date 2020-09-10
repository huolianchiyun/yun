package com.zhangbin.yun.yunrights.auto;


import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({YunRightsImportSelector.class, AutoBeanPostProcessor.class})
public @interface EnableYunRights {
    boolean autoRegister() default true;
}
