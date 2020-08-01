package com.zhangbin.yun.yunrights.modules.logging.annotation;

import com.zhangbin.yun.yunrights.modules.logging.enums.LogActionType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    String value() default "";

    /**
     * 是否启用
     *
     * @return
     */
    boolean enable() default true;

    LogActionType type() default LogActionType.SELECT;
}
