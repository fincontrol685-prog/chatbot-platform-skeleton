package com.br.chatbotplatformskeleton.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Configuration for Spring Cache with Redis backend.
 * Enables caching for performance optimization in Phase 3.
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * Configures Redis cache with default TTL of 1 hour.
     * Can be overridden per cache using @Cacheable annotations.
     *
     * @param connectionFactory Redis connection factory
     * @return Configured RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}

