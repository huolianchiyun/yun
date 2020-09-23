package com.zhangbin.yun.yunrights.modules.common.config.cache;


import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import com.zhangbin.yun.yunrights.modules.common.utils.StringUtils;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static com.zhangbin.yun.yunrights.modules.common.config.cache.CacheKey.*;

public final class MyRedisCacheWriter implements RedisCacheWriter {
    public static final Logger log = LoggerFactory.getLogger(MyRedisCacheWriter.class);
    private static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private static UserService userService;
    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;

    /**
     * Create new {@link RedisCacheWriter} without locking behavior.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new instance of {@link MyRedisCacheWriter}.
     */
    static RedisCacheWriter nonLockingRedisCacheWriter(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return new MyRedisCacheWriter(connectionFactory);
    }

    /**
     * Create new {@link RedisCacheWriter} with locking behavior.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new instance of {@link MyRedisCacheWriter}.
     */
    static RedisCacheWriter lockingRedisCacheWriter(RedisConnectionFactory connectionFactory) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");

        return new MyRedisCacheWriter(connectionFactory, Duration.ofMillis(50));
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     */
    private MyRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO);
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime         sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *                          to disable locking.
     */
    private MyRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
    }

    /* 扩展了用户关联 hash 存储
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#put(java.lang.String, byte[], byte[], java.time.Duration)
     */
    @Override
    public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        String keyStr = stringRedisSerializer.deserialize(key);
        if (keyStr.contains(BIND_USER_FLAG)) {
            String username = extractUsernameForm(keyStr);
            // 如果未提取到，则不缓存
            if (!StringUtils.isNotEmpty(username)) return;
            byte[] hKey = stringRedisSerializer.serialize(BIND_USER_HASH_KEY_PREFIX + username);
            execute(name, connection -> {
                connection.hSet(hKey, key, value);
                if (shouldExpireWithin(ttl)) {
                    connection.expire(hKey, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS).getExpirationTimeInSeconds());
                }
                return "OK";
            });
            return;
        }

        execute(name, connection -> {
            if (shouldExpireWithin(ttl)) {
                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), SetOption.upsert());
            } else {
                connection.set(key, value);
            }
            return "OK";
        });
    }

    /* 扩展了用户关联 hash 获取
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#get(java.lang.String, byte[])
     */
    @Override
    public byte[] get(String name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        String keyStr = stringRedisSerializer.deserialize(key);
        if (keyStr.contains(BIND_USER_FLAG)) {
            String username = extractUsernameForm(keyStr);
            // 如果未提取到，则不缓存
            if (StringUtils.isNotEmpty(username)) {
                byte[] hKey = stringRedisSerializer.serialize(BIND_USER_HASH_KEY_PREFIX + username);
                return execute(name, connection -> connection.hGet(hKey, key));
            }
        }
        return execute(name, connection -> connection.get(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#putIfAbsent(java.lang.String, byte[], byte[], java.time.Duration)
     */
    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        return execute(name, connection -> {
            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }
            try {
                boolean put;
                if (shouldExpireWithin(ttl)) {
                    put = connection.set(key, value, Expiration.from(ttl), SetOption.ifAbsent());
                } else {
                    put = connection.setNX(key, value);
                }
                if (put) {
                    return null;
                }
                return connection.get(key);
            } finally {

                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#remove(java.lang.String, byte[])
     */
    @Override
    public void remove(String name, byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        execute(name, connection -> connection.del(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#clean(java.lang.String, byte[])
     */
    @Override
    public void clean(String name, byte[] pattern) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");

        execute(name, connection -> {
            boolean wasLocked = false;
            try {
                if (isLockingCacheWriter()) {
                    doLock(name, connection);
                    wasLocked = true;
                }
                byte[][] keys = Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())
                        .toArray(new byte[0][]);
                if (keys.length > 0) {
                    connection.del(keys);
                }
            } finally {
                if (wasLocked && isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
            return "OK";
        });
    }

    /**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     */
    void lock(String name) {
        execute(name, connection -> doLock(name, connection));
    }

    /**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     */
    void unlock(String name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    private Boolean doLock(String name, RedisConnection connection) {
        return connection.setNX(createCacheLockKey(name), new byte[0]);
    }

    private Long doUnlock(String name, RedisConnection connection) {
        return connection.del(createCacheLockKey(name));
    }

    boolean doCheckLock(String name, RedisConnection connection) {
        return connection.exists(createCacheLockKey(name));
    }

    /**
     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
     */
    private boolean isLockingCacheWriter() {
        return !sleepTime.isZero() && !sleepTime.isNegative();
    }

    private <T> T execute(String name, Function<RedisConnection, T> callback) {
        RedisConnection connection = connectionFactory.getConnection();
        try {
            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }

    private void executeLockFree(Consumer<RedisConnection> callback) {
        RedisConnection connection = connectionFactory.getConnection();
        try {
            callback.accept(connection);
        } finally {
            connection.close();
        }
    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {
        if (!isLockingCacheWriter()) {
            return;
        }
        try {

            while (doCheckLock(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {
            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();

            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name),
                    ex);
        }
    }

    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    private static byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }

    private static String extractUsernameForm(String key) {
        String username = null;
        try {
            String[] splitKeys = key.split(":+");
            if (splitKeys.length >= 2) {
                for (int i = splitKeys.length - 2; i >= 0; i--) {
                    if (Username.equalsIgnoreCase(splitKeys[i])) {
                        username = splitKeys[++i];
                        break;
                    } else if (UserId.equalsIgnoreCase(splitKeys[i])) {
                        username = userService.queryUsernameById(Long.valueOf(splitKeys[++i]));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("*** *** MyRedisCacheWriter#extractUserIdForm({}) 执行异常：", key);
            e.printStackTrace();
        }
        return username;
    }
}
