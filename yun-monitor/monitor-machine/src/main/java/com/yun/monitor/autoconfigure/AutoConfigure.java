package com.yun.monitor.autoconfigure;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = AutoConfigure.SCAN_PACKAGE)
public class AutoConfigure {
     static final String SCAN_PACKAGE = "com/yun/monitor";
}
