package com.hlcy.yun.sys.modules.rights.datarights;


import com.hlcy.yun.sys.modules.rights.datarights.dialect.Dialect;
import com.hlcy.yun.sys.modules.rights.datarights.exception.DataRightsException;
import com.hlcy.yun.sys.modules.rights.datarights.dialect.AbstractDialect;
import com.hlcy.yun.sys.modules.rights.datarights.dialect.helper.MySqlDialect;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.StringUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基础方言信息
 */
public class DataRightsAutoDialect {

    private static Map<String, Class<? extends Dialect>> dialectAliasMap = new HashMap<>();

    public static void registerDialectAlias(String alias, Class<? extends Dialect> dialectClass){
        dialectAliasMap.put(alias, dialectClass);
    }

    static {
        //注册别名
        registerDialectAlias("mysql", MySqlDialect.class);
        registerDialectAlias("mariadb", MySqlDialect.class);
        registerDialectAlias("sqlite", MySqlDialect.class);
        //神通数据库
        registerDialectAlias("oscar", MySqlDialect.class);
    }

    //自动获取dialect,如果没有setProperties或setSqlUtilConfig，也可以正常进行
    private boolean autoDialect = true;
    //多数据源时，获取jdbcurl后是否关闭数据源
    private boolean closeConn = true;
    //属性配置
    private Properties properties;
    //缓存
    private Map<String, AbstractDialect> urlDialectMap = new ConcurrentHashMap<>();
    private ReentrantLock lock = new ReentrantLock();
    private AbstractDialect delegate;
    private ThreadLocal<AbstractDialect> dialectThreadLocal = new ThreadLocal<>();

    //多数据动态获取时，每次需要初始化
    public void initDelegateDialect(MappedStatement ms) {
        if (delegate == null) {
            if (autoDialect) {
                this.delegate = getDialect(ms);
            } else {
                dialectThreadLocal.set(getDialect(ms));
            }
        }
    }

    //获取当前的代理对象
    public AbstractDialect getDelegate() {
        if (delegate != null) {
            return delegate;
        }
        return dialectThreadLocal.get();
    }

    //移除代理对象
    public void clearDelegate() {
        dialectThreadLocal.remove();
    }

    private String fromJdbcUrl(String jdbcUrl) {
        for (String dialect : dialectAliasMap.keySet()) {
            if (jdbcUrl.contains(":" + dialect + ":")) {
                return dialect;
            }
        }
        return null;
    }

    /**
     * 反射类
     *
     * @param className
     * @return
     * @throws Exception
     */
    private Class resolveDialectClass(String className) throws Exception {
        if (dialectAliasMap.containsKey(className.toLowerCase())) {
            return dialectAliasMap.get(className.toLowerCase());
        } else {
            return Class.forName(className);
        }
    }

    /**
     * 初始化 helper
     *
     * @param dialectClass
     * @param properties
     */
    private AbstractDialect initDialect(String dialectClass, Properties properties) {
        AbstractDialect dialect;
        if (StringUtils.isEmpty(dialectClass)) {
            throw new DataRightsException("使用 PageHelper 分页插件时，必须设置 helper 属性");
        }
        try {
            Class sqlDialectClass = resolveDialectClass(dialectClass);
            if (AbstractDialect.class.isAssignableFrom(sqlDialectClass)) {
                dialect = (AbstractDialect) sqlDialectClass.newInstance();
            } else {
                throw new DataRightsException("使用 PageHelper 时，方言必须是实现 " + AbstractDialect.class.getCanonicalName() + " 接口的实现类!");
            }
        } catch (Exception e) {
            throw new DataRightsException("初始化 helper [" + dialectClass + "]时出错:" + e.getMessage(), e);
        }
        dialect.setProperties(properties);
        return dialect;
    }

    /**
     * 获取url
     *
     * @param dataSource
     * @return
     */
    private String getUrl(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new DataRightsException(e);
        } finally {
            if (conn != null) {
                try {
                    if (closeConn) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * 根据 jdbcUrl 获取数据库方言
     *
     * @param ms
     * @return
     */
    private AbstractDialect getDialect(MappedStatement ms) {
        //改为对dataSource做缓存
        DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
        String url = getUrl(dataSource);
        if (urlDialectMap.containsKey(url)) {
            return urlDialectMap.get(url);
        }
        try {
            lock.lock();
            if (urlDialectMap.containsKey(url)) {
                return urlDialectMap.get(url);
            }
            if (StringUtils.isEmpty(url)) {
                throw new DataRightsException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
            }
            String dialectStr = fromJdbcUrl(url);
            if (dialectStr == null) {
                throw new DataRightsException("无法自动获取数据库类型，请通过 helperDialect 参数指定!");
            }
            AbstractDialect dialect = initDialect(dialectStr, properties);
            urlDialectMap.put(url, dialect);
            return dialect;
        } finally {
            lock.unlock();
        }
    }

    public void setProperties(Properties properties) {
        //多数据源时，获取 jdbcurl 后是否关闭数据源
        String closeConn = properties.getProperty("closeConn");
        if (StringUtils.hasText(closeConn)) {
            this.closeConn = Boolean.parseBoolean(closeConn);
        }
        String dialectAlias = properties.getProperty("dialectAlias");
        if (StringUtils.hasText(dialectAlias)) {
            String[] alias = dialectAlias.split(";");
            for (String s : alias) {
                String[] kv = s.split("=");
                if (kv.length != 2) {
                    throw new IllegalArgumentException("dialectAlias 参数配置错误，" +
                            "请按照 alias1=xx.dialectClass;alias2=dialectClass2 的形式进行配置!");
                }
                for (int j = 0; j < kv.length; j++) {
                    try {
                        Class<? extends Dialect> dialectClass = (Class<? extends Dialect>) Class.forName(kv[1]);
                        //允许覆盖已有的实现
                        registerDialectAlias(kv[0], dialectClass);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("请确保 dialectAlias 配置的 Dialect 实现类存在!", e);
                    }
                }
            }
        }
        //指定的 Helper 数据库方言，和  不同
        String dialect = properties.getProperty("helperDialect");
        //运行时获取数据源
        String runtimeDialect = properties.getProperty("autoRuntimeDialect");
        //1.动态多数据源
        if (StringUtils.hasText(runtimeDialect) && "TRUE".equalsIgnoreCase(runtimeDialect)) {
            this.autoDialect = false;
            this.properties = properties;
        }
        //2.动态获取方言
        else if (StringUtils.isEmpty(dialect)) {
            autoDialect = true;
            this.properties = properties;
        }
        //3.指定方言
        else {
            autoDialect = false;
            this.delegate = initDialect(dialect, properties);
        }
    }
}
