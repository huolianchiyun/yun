package com.zhangbin.yun.yunrights.modules.rights.datarights.dialect;


import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Properties;

/**
 * 数据库方言，针对不同数据库进行实现
 */
public interface Dialect {

  /**
   * 跳过数据权限查询
   *
   * @param ms              MappedStatement
   * @param parameterObject 方法参数
   * @return true 跳过，返回默认查询结果，false 执行权限查询
   */
  boolean skip(MappedStatement ms, Object parameterObject);

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
   * 执行数据权限前，返回 true 会进行数据权限查询，false 会返回默认查询结果
   *
   * @param ms              MappedStatement
   * @param parameterObject 方法参数
   * @return
   */
  boolean beforeRightsQuery(MappedStatement ms, Object parameterObject);

  /**
   * 生成权限查询 sql
   *
   * @param ms              MappedStatement
   * @param boundSql        绑定 SQL 对象
   * @param parameterObject 方法参数
   * @param cacheKey         权限缓存 key
   * @return
   */
  String getPermissionSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, CacheKey cacheKey);

  /**
   * 数据权限查询后，处理查询结果，拦截器中直接 return 该方法的返回值
   *
   * @param rightsList       权限查询结果
   * @param parameterObject 方法参数
   * @return
   */
  Object afterRightsQuery(List rightsList, Object parameterObject);

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
