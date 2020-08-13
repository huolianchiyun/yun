package com.zhangbin.yun.yunrights.modules.rights.common.data_rights;

public class DataRightsHelper implements Dialect {
    protected static final ThreadLocal<String> LOCAL_PAGE = new ThreadLocal<>();


    @Override
    public Boolean isRights() {
        return null;
    }

    @Override
    public String wrapOriginSql(String originSql) {
        return null;
    }
}
