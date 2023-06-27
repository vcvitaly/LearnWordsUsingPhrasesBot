package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * CacheConfig.
 *
 * @author Vitalii Chura
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String DEFINITION_CACHE = "definition_cache";

    @Value("${cache.caffeine.ttl}")
    private Duration ttl;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterAccess(ttl);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
