package com.yun.sys.modules.common.xcache;

import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import java.util.*;


/**
 * {@link org.springframework.cache.CacheManager} backed by a {@link RedisCache Redis} cache.
 * <p />
 * This cache manager creates caches by default upon first write. Empty caches are not visible on Redis due to how Redis
 * represents empty data structures.
 * <p />
 * Caches requiring a different {@link RedisCacheConfiguration} than the default configuration can be specified via
 * {@link RedisCacheManager.RedisCacheManagerBuilder#withInitialCacheConfigurations(Map)}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author zhangbin
 * @since 2.0
 * @see RedisCacheConfiguration
 * @see RedisCacheWriter
 */
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private final Map<String, RedisCacheConfiguration> initialCacheConfiguration;
    private final boolean allowInFlightCacheCreation;

    /**
     * Creates new {@link org.springframework.data.redis.cache.RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param allowInFlightCacheCreation allow create unconfigured caches.
     * @since 2.0.4
     */
    private RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                              boolean allowInFlightCacheCreation) {
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.initialCacheConfiguration = new LinkedHashMap<>();
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        this(cacheWriter, defaultCacheConfiguration, true);
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheNames optional set of known cache names that will be created with given
     *          {@literal defaultCacheConfiguration}.
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                             String... initialCacheNames) {
        this(cacheWriter, defaultCacheConfiguration, true, initialCacheNames);
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param allowInFlightCacheCreation if set to {@literal true} no new caches can be acquire at runtime but limited to
     *          the given list of initial cache names.
     * @param initialCacheNames optional set of known cache names that will be created with given
     *          {@literal defaultCacheConfiguration}.
     * @since 2.0.4
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                             boolean allowInFlightCacheCreation, String... initialCacheNames) {
        this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation);
        for (String cacheName : initialCacheNames) {
            this.initialCacheConfiguration.put(cacheName, defaultCacheConfiguration);
        }
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
     *          Must not be {@literal null}.
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                             Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        this(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, true);
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
     *          Must not be {@literal null}.
     * @param allowInFlightCacheCreation if set to {@literal false} this cache manager is limited to the initial cache
     *          configurations and will not create new caches at runtime.
     * @since 2.0.4
     */
    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
                             Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation);
        Assert.notNull(initialCacheConfigurations, "InitialCacheConfigurations must not be null!");
        this.initialCacheConfiguration.putAll(initialCacheConfigurations);
    }

    /**
     * Create a new {@link RedisCacheManager} with defaults applied.
     * <dl>
     * <dt>locking</dt>
     * <dd>disabled</dd>
     * <dt>cache configuration</dt>
     * <dd>{@link RedisCacheConfiguration#defaultCacheConfig()}</dd>
     * <dt>initial caches</dt>
     * <dd>none</dd>
     * <dt>transaction aware</dt>
     * <dd>no</dd>
     * <dt>in-flight cache creation</dt>
     * <dd>enabled</dd>
     * </dl>
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new instance of {@link RedisCacheManager}.
     */
    public static RedisCacheManager create(RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        return new RedisCacheManager(new DefaultRedisCacheWriter(connectionFactory),
                RedisCacheConfiguration.defaultCacheConfig());
    }

    /**
     * Entry point for builder style {@link RedisCacheManager} configuration.
     *
     * @return new {@link RedisCacheManager.RedisCacheManagerBuilder}.
     * @since 2.3
     */
    public static RedisCacheManager.RedisCacheManagerBuilder builder() {
        return new RedisCacheManagerBuilder();
    }

    /**
     * Entry point for builder style {@link RedisCacheManager} configuration.
     *
     * @param connectionFactory must not be {@literal null}.
     * @return new {@link RedisCacheManagerBuilder}.
     */
    public static RedisCacheManager.RedisCacheManagerBuilder builder(RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
    }

    /**
     * Entry point for builder style {@link RedisCacheManager} configuration.
     *
     * @param cacheWriter must not be {@literal null}.
     * @return new {@link RedisCacheManager.RedisCacheManagerBuilder}.
     */
    public static RedisCacheManager.RedisCacheManagerBuilder builder(RedisCacheWriter cacheWriter) {
        Assert.notNull(cacheWriter, "CacheWriter must not be null!");
        return RedisCacheManager.RedisCacheManagerBuilder.fromCacheWriter(cacheWriter);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
     */
    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
            caches.add(createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.support.AbstractCacheManager#getMissingCache(java.lang.String)
     */
    @Override
    protected RedisCache getMissingCache(String name) {
        return allowInFlightCacheCreation ? createRedisCache(name, defaultCacheConfig) : null;
    }

    /**
     * @return unmodifiable {@link Map} containing cache name / configuration pairs. Never {@literal null}.
     */
    public Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(getCacheNames().size());
        getCacheNames().forEach(it -> {
            RedisCache cache = RedisCache.class.cast(lookupCache(it));
            configurationMap.put(it, cache != null ? cache.getCacheConfiguration() : null);
        });

        return Collections.unmodifiableMap(configurationMap);
    }

    /**
     * Configuration hook for creating {@link RedisCache} with given name and {@code cacheConfig}.
     *
     * @param name must not be {@literal null}.
     * @param cacheConfig can be {@literal null}.
     * @return never {@literal null}.
     */
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        return new RedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    /**
     * Configurator for creating {@link org.springframework.data.redis.cache.RedisCacheManager}.
     *
     * @author Christoph Strobl
     * @author Mark Paluch
     * @author Kezhu Wang
     * @since 2.0
     */
    public static class RedisCacheManagerBuilder {

        private @Nullable RedisCacheWriter cacheWriter;
        private RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        private final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        private boolean enableTransactions;
        boolean allowInFlightCacheCreation = true;

        private RedisCacheManagerBuilder() {}

        private RedisCacheManagerBuilder(RedisCacheWriter cacheWriter) {
            this.cacheWriter = cacheWriter;
        }

        /**
         * Entry point for builder style {@link RedisCacheManager} configuration.
         *
         * @param connectionFactory must not be {@literal null}.
         * @return new {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public static RedisCacheManager.RedisCacheManagerBuilder fromConnectionFactory(RedisConnectionFactory connectionFactory) {
            Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
            return new RedisCacheManager.RedisCacheManagerBuilder(new DefaultRedisCacheWriter(connectionFactory));
        }

        /**
         * Entry point for builder style {@link RedisCacheManager} configuration.
         *
         * @param cacheWriter must not be {@literal null}.
         * @return new {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public static RedisCacheManager.RedisCacheManagerBuilder fromCacheWriter(RedisCacheWriter cacheWriter) {
            Assert.notNull(cacheWriter, "CacheWriter must not be null!");
            return new RedisCacheManager.RedisCacheManagerBuilder(cacheWriter);
        }

        /**
         * Define a default {@link RedisCacheConfiguration} applied to dynamically created {@link RedisCache}s.
         *
         * @param defaultCacheConfiguration must not be {@literal null}.
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public RedisCacheManager.RedisCacheManagerBuilder cacheDefaults(RedisCacheConfiguration defaultCacheConfiguration) {
            Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");
            this.defaultCacheConfiguration = defaultCacheConfiguration;
            return this;
        }

        /**
         * Configure a {@link RedisCacheWriter}.
         *
         * @param cacheWriter must not be {@literal null}.
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         * @since 2.3
         */
        public RedisCacheManager.RedisCacheManagerBuilder cacheWriter(RedisCacheWriter cacheWriter) {
            Assert.notNull(cacheWriter, "CacheWriter must not be null!");
            this.cacheWriter = cacheWriter;
            return this;
        }

        /**
         * Enable {@link RedisCache}s to synchronize cache put/evict operations with ongoing Spring-managed transactions.
         *
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public RedisCacheManager.RedisCacheManagerBuilder transactionAware() {
            this.enableTransactions = true;
            return this;
        }

        /**
         * Append a {@link Set} of cache names to be pre initialized with current {@link RedisCacheConfiguration}.
         * <strong>NOTE:</strong> This calls depends on {@link #cacheDefaults(RedisCacheConfiguration)} using whatever
         * default {@link RedisCacheConfiguration} is present at the time of invoking this method.
         *
         * @param cacheNames must not be {@literal null}.
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public RedisCacheManager.RedisCacheManagerBuilder initialCacheNames(Set<String> cacheNames) {
            Assert.notNull(cacheNames, "CacheNames must not be null!");
            cacheNames.forEach(it -> withCacheConfiguration(it, defaultCacheConfiguration));
            return this;
        }

        /**
         * Append a {@link Map} of cache name/{@link RedisCacheConfiguration} pairs to be pre initialized.
         *
         * @param cacheConfigurations must not be {@literal null}.
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         */
        public RedisCacheManager.RedisCacheManagerBuilder withInitialCacheConfigurations(
                Map<String, RedisCacheConfiguration> cacheConfigurations) {
            Assert.notNull(cacheConfigurations, "CacheConfigurations must not be null!");
            cacheConfigurations.forEach((cacheName, configuration) -> Assert.notNull(configuration,
                    String.format("RedisCacheConfiguration for cache %s must not be null!", cacheName)));
            this.initialCaches.putAll(cacheConfigurations);
            return this;
        }

        /**
         * @param cacheName
         * @param cacheConfiguration
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         * @since 2.2
         */
        public RedisCacheManager.RedisCacheManagerBuilder withCacheConfiguration(String cacheName, RedisCacheConfiguration cacheConfiguration) {
            Assert.notNull(cacheName, "CacheName must not be null!");
            Assert.notNull(cacheConfiguration, "CacheConfiguration must not be null!");
            this.initialCaches.put(cacheName, cacheConfiguration);
            return this;
        }

        /**
         * Disable in-flight {@link org.springframework.cache.Cache} creation for unconfigured caches.
         * <p />
         * {@link RedisCacheManager#getMissingCache(String)} returns {@literal null} for any unconfigured
         * {@link org.springframework.cache.Cache} instead of a new {@link RedisCache} instance. This allows eg.
         * {@link org.springframework.cache.support.CompositeCacheManager} to chime in.
         *
         * @return this {@link RedisCacheManager.RedisCacheManagerBuilder}.
         * @since 2.0.4
         */
        public RedisCacheManager.RedisCacheManagerBuilder disableCreateOnMissingCache() {
            this.allowInFlightCacheCreation = false;
            return this;
        }

        /**
         * Get the {@link Set} of cache names for which the builder holds {@link RedisCacheConfiguration configuration}.
         *
         * @return an unmodifiable {@link Set} holding the name of caches for which a {@link RedisCacheConfiguration
         *         configuration} has been set.
         * @since 2.2
         */
        public Set<String> getConfiguredCaches() {
            return Collections.unmodifiableSet(this.initialCaches.keySet());
        }

        /**
         * Get the {@link RedisCacheConfiguration} for a given cache by its name.
         *
         * @param cacheName must not be {@literal null}.
         * @return {@link Optional#empty()} if no {@link RedisCacheConfiguration} set for the given cache name.
         * @since 2.2
         */
        public Optional<RedisCacheConfiguration> getCacheConfigurationFor(String cacheName) {
            return Optional.ofNullable(this.initialCaches.get(cacheName));
        }

        /**
         * Create new instance of {@link RedisCacheManager} with configuration options applied.
         *
         * @return new instance of {@link RedisCacheManager}.
         */
        public RedisCacheManager build() {
            Assert.state(cacheWriter != null, "CacheWriter must not be null! You can provide one via 'RedisCacheManagerBuilder#cacheWriter(RedisCacheWriter)'.");
            RedisCacheManager cm = new RedisCacheManager(cacheWriter, defaultCacheConfiguration, initialCaches, allowInFlightCacheCreation);
            cm.setTransactionAware(enableTransactions);
            return cm;
        }
    }
}
