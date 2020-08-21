package com.zhangbin.yun.yunrights.modules.rights.common.datarights;


/**
 * 数据库方言，针对不同数据库进行实现
 */
public interface Dialect {

  Boolean isRights();

  String wrapOriginSql(String originSql);

}
