/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.config.RedisConfig
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.data.redis.cache.RedisCacheConfiguration
 *  org.springframework.data.redis.cache.RedisCacheManager
 *  org.springframework.data.redis.connection.RedisConnectionFactory
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
 *  org.springframework.data.redis.serializer.RedisSerializationContext$SerializationPair
 *  org.springframework.data.redis.serializer.RedisSerializer
 *  org.springframework.data.redis.serializer.StringRedisSerializer
 */
package com.example.graderbackend.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1L)).disableCachingNullValues().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((RedisSerializer)serializer));
        return RedisCacheManager.builder((RedisConnectionFactory)connectionFactory).cacheDefaults(config).build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate();
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer((RedisSerializer)new StringRedisSerializer());
        template.setValueSerializer((RedisSerializer)serializer);
        template.setHashKeySerializer((RedisSerializer)new StringRedisSerializer());
        template.setHashValueSerializer((RedisSerializer)serializer);
        template.afterPropertiesSet();
        return template;
    }
}

