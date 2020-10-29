/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhangbin.yun.sys.modules.common.xcache;

import com.zhangbin.yun.common.utils.str.StringUtils;
import com.zhangbin.yun.sys.modules.rights.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static com.zhangbin.yun.sys.modules.common.xcache.CacheKey.*;

/**
 * {@link org.springframework.cache.Cache} implementation using for Redis as underlying store.
 * <p/>
 * Use {@link RedisCacheManager} to create {@link RedisCache} instances.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @see RedisCacheConfiguration
 * @see RedisCacheWriter
 * @since 2.0
 */
public class RedisCache extends AbstractValueAdaptingCache {
    public static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private static final byte[] BINARY_NULL_VALUE = RedisSerializer.java().serialize(NullValue.INSTANCE);
    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration cacheConfig;
    private final ConversionService conversionService;
    private static UserService userService;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected RedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {

        super(cacheConfig.getAllowCacheNullValues());

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(cacheConfig, "CacheConfig must not be null!");

        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;
        this.conversionService = cacheConfig.getConversionService();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.support.AbstractValueAdaptingCache#lookup(java.lang.Object)
     */
    @Override
    protected Object lookup(Object key) {
        byte[] value;
        if (name.startsWith(HSet)) {
            String hKey;
            JUMP_OUT_BLOCK:
            {
                if (name.contains(BIND_USER)) {
                    String username = extractUsernameForm(key.toString());
                    if (StringUtils.isBlank(username)) return null;
                    hKey = BIND_USER_HASH_KEY_PREFIX + username;
                    key = name.substring(name.indexOf(BIND_USER) + BIND_USER.length()) + ":" + key;
                    break JUMP_OUT_BLOCK;
                }
                hKey = name.substring(name.indexOf(HSet) + HSet.length());
                if (StringUtils.isBlank(hKey)) return null;
            }
            value = cacheWriter.hget(name, createAndConvertCacheKey(hKey), createAndConvertCacheKey(key));
        } else {
            value = cacheWriter.get(name, createAndConvertCacheKey(key));
        }
        if (value == null) {
            return null;
        }
        return deserializeCacheValue(value);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#getNativeCache()
     */
    @Override
    public RedisCacheWriter getNativeCache() {
        return this.cacheWriter;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#get(java.lang.Object, java.util.concurrent.Callable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper result = get(key);
        if (result != null) {
            return (T) result.get();
        }
        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public void put(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }
        if (name.startsWith(HSet)) {
            String hKey;
            JUMP_OUT_BLOCK:
            {
                if (name.startsWith(BIND_USER)) {
                    String username = extractUsernameForm((String) key);
                    if (StringUtils.isBlank(username)) return;
                    hKey = BIND_USER_HASH_KEY_PREFIX + username;
                    key = name.substring(name.indexOf(BIND_USER) + BIND_USER.length()) + ":" + key;
                    break JUMP_OUT_BLOCK;
                }
                hKey = name.substring(name.indexOf(HSet) + HSet.length());
                if (StringUtils.isBlank(hKey)) return;
            }
            cacheWriter.hset(name, createAndConvertCacheKey(hKey), createAndConvertCacheKey(key), serializeCacheValue(cacheValue), cacheConfig.getTtl());
            return;
        }

        cacheWriter.put(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), cacheConfig.getTtl());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#putIfAbsent(java.lang.Object, java.lang.Object)
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }
        byte[] result = cacheWriter.putIfAbsent(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue),
                cacheConfig.getTtl());
        if (result == null) {
            return null;
        }
        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#evict(java.lang.Object)
     */
    @Override
    public void evict(Object key) {
        cacheWriter.remove(name, createAndConvertCacheKey(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.Cache#clear()
     */
    @Override
    public void clear() {

        byte[] pattern = conversionService.convert(createCacheKey("*"), byte[].class);
        cacheWriter.clean(name, pattern);
    }

    /**
     * Get {@link RedisCacheConfiguration} used.
     *
     * @return immutable {@link RedisCacheConfiguration}. Never {@literal null}.
     */
    public RedisCacheConfiguration getCacheConfiguration() {
        return cacheConfig;
    }

    /**
     * Customization hook called before passing object to
     * {@link RedisSerializer}.
     *
     * @param value can be {@literal null}.
     * @return preprocessed value. Can be {@literal null}.
     */
    @Nullable
    protected Object preProcessCacheValue(@Nullable Object value) {

        if (value != null) {
            return value;
        }

        return isAllowNullValues() ? NullValue.INSTANCE : null;
    }

    /**
     * Serialize the key.
     *
     * @param cacheKey must not be {@literal null}.
     * @return never {@literal null}.
     */
    protected byte[] serializeCacheKey(String cacheKey) {
        return ByteUtils.getBytes(cacheConfig.getKeySerializationPair().write(cacheKey));
    }

    /**
     * Serialize the value to cache.
     *
     * @param value must not be {@literal null}.
     * @return never {@literal null}.
     */
    protected byte[] serializeCacheValue(Object value) {

        if (isAllowNullValues() && value instanceof NullValue) {
            return BINARY_NULL_VALUE;
        }

        return ByteUtils.getBytes(cacheConfig.getValueSerializationPair().write(value));
    }

    /**
     * Deserialize the given value to the actual cache value.
     *
     * @param value must not be {@literal null}.
     * @return can be {@literal null}.
     */
    @Nullable
    protected Object deserializeCacheValue(byte[] value) {

        if (isAllowNullValues() && ObjectUtils.nullSafeEquals(value, BINARY_NULL_VALUE)) {
            return NullValue.INSTANCE;
        }

        return cacheConfig.getValueSerializationPair().read(ByteBuffer.wrap(value));
    }

    /**
     * Customization hook for creating cache key before it gets serialized.
     *
     * @param key will never be {@literal null}.
     * @return never {@literal null}.
     */
    protected String createCacheKey(Object key) {

        String convertedKey = convertKey(key);

        if (!cacheConfig.usePrefix()) {
            return convertedKey;
        }

        return prefixCacheKey(convertedKey);
    }

    /**
     * Convert {@code key} to a {@link String} representation used for cache key creation.
     *
     * @param key will never be {@literal null}.
     * @return never {@literal null}.
     * @throws IllegalStateException if {@code key} cannot be converted to {@link String}.
     */
    protected String convertKey(Object key) {

        if (key instanceof String) {
            return (String) key;
        }

        TypeDescriptor source = TypeDescriptor.forObject(key);

        if (conversionService.canConvert(source, TypeDescriptor.valueOf(String.class))) {
            try {
                return conversionService.convert(key, String.class);
            } catch (ConversionFailedException e) {

                // may fail if the given key is a collection
                if (isCollectionLikeOrMap(source)) {
                    return convertCollectionLikeOrMapKey(key, source);
                }

                throw e;
            }
        }

        Method toString = ReflectionUtils.findMethod(key.getClass(), "toString");

        if (toString != null && !Object.class.equals(toString.getDeclaringClass())) {
            return key.toString();
        }

        throw new IllegalStateException(String.format(
                "Cannot convert cache key %s to String. Please register a suitable Converter via 'RedisCacheConfiguration.configureKeyConverters(...)' or override '%s.toString()'.",
                source, key.getClass().getSimpleName()));
    }

    private String convertCollectionLikeOrMapKey(Object key, TypeDescriptor source) {

        if (source.isMap()) {

            StringBuilder target = new StringBuilder("{");

            for (Entry<?, ?> entry : ((Map<?, ?>) key).entrySet()) {
                target.append(convertKey(entry.getKey())).append("=").append(convertKey(entry.getValue()));
            }
            target.append("}");

            return target.toString();
        } else if (source.isCollection() || source.isArray()) {

            StringJoiner sj = new StringJoiner(",");

            Collection<?> collection = source.isCollection() ? (Collection<?>) key
                    : Arrays.asList(ObjectUtils.toObjectArray(key));

            for (Object val : collection) {
                sj.add(convertKey(val));
            }
            return "[" + sj.toString() + "]";
        }

        throw new IllegalArgumentException(String.format("Cannot convert cache key %s to String.", key));
    }

    private boolean isCollectionLikeOrMap(TypeDescriptor source) {
        return source.isArray() || source.isCollection() || source.isMap();
    }

    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }

    private String prefixCacheKey(String key) {
        if (name.startsWith(HSet)) {
            return key;
        }
        // allow contextual cache names by computing the key prefix on every call.
        return cacheConfig.getKeyPrefixFor(name) + key;
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    private static String extractUsernameForm(String key) {
        String username = null;
        try {
            String[] splitKeys = key.split(":+");
            if (splitKeys.length >= 2) {
                for (int i = splitKeys.length - 2; i >= 0; i--) {
                    if (USERNAME.equalsIgnoreCase(splitKeys[i])) {
                        username = splitKeys[++i];
                        break;
                    } else if (UserId.equalsIgnoreCase(splitKeys[i])) {
                        username = userService.queryUsernameById(Long.valueOf(splitKeys[++i]));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("*** *** RedisCache#extractUserIdForm({}) 执行异常：", key);
            e.printStackTrace();
        }
        return username;
    }
}
