package com.hlcy.yun.monitor.autoconfigure;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigure.class)
public @interface EnableMonitor {
}
