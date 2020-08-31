package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect;


import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import java.util.Properties;

/**
 * 数据库方言，针对不同数据库进行实现
 */
public interface Dialect {

  /**
   * 跳过数据权限查询
   *
   * @param ms              MappedStatement
   * @return true 跳过，false 执行权限查询
   */
  boolean skip(MappedStatement ms);

  /**
   * 跳过数据权限修改
   *
   * @param ms              MappedStatement
   * @return true 跳过，false 执行权限
   */
  boolean skipForUpdate(MappedStatement ms);

  /**
   * 处理查询参数对象
   *
   * @param ms              MappedStatement
   * @param parameterObject
   * @param boundSql
   * @param pageKey
   * @return
   */
  Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey);

  /**
   * 执行数据权限前
   *
   * @param ms              MappedStatement
   * @param parameterObject 方法参数
   * @return 返回 true 会进行数据权限查询
   */
  boolean beforeRightsQuery(MappedStatement ms, Object parameterObject);

  /**
   * 生成权限查询 sql
   *
   * @param boundSql        绑定 SQL 对象
   * @param parameterObject 方法参数
   * @return
   */
  String getPermissionSql( BoundSql boundSql, Object parameterObject);


  /**
   * 完成所有任务后
   */
  void afterAll();

  /**
   * 设置参数
   *
   * @param properties 插件属性
   */
  void setProperties(Properties properties);

}
