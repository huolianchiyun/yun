package com.zhangbin.yun.yunrights.modules.rights.datarights.exception;

/**
 * 数据权限插件异常
 */
public class DataRightsException extends RuntimeException {
    public DataRightsException() {
        super();
    }

    public DataRightsException(String message) {
        super(message);
    }

    public DataRightsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataRightsException(Throwable cause) {
        super(cause);
    }
}
