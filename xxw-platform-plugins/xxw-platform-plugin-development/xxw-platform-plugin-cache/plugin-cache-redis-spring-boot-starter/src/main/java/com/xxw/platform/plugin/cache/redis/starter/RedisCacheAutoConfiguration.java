package com.xxw.platform.plugin.cache.redis.starter;

import com.xxw.platform.plugin.cache.sdk.redis.operator.DefaultRedisCacheOperator;
import com.xxw.platform.plugin.cache.sdk.redis.operator.DefaultStringRedisCacheOperator;
import com.xxw.platform.plugin.cache.sdk.redis.serializer.FastJson2JsonRedisSerializer;
import com.xxw.platform.plugin.cache.sdk.redis.util.CreateRedisTemplateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 基于redis缓存的默认配置，默认提供两个RedisTemplate工具类，其他的各个模块自行配置
 *
 * @author liaoxiting
 * @date 2021/1/31 20:33
 */
@Configuration
public class RedisCacheAutoConfiguration {

    /**
     * Redis的value序列化器
     *
     * @author liaoxiting
     * @date 2021/1/31 20:44
     */
    @Bean
    public RedisSerializer<?> fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<>(Object.class);
    }

    /**
     * value是object类型的redis操作类
     *
     * @author liaoxiting
     * @date 2021/1/31 20:45
     */
    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return CreateRedisTemplateUtil.createObject(redisConnectionFactory);
    }

    /**
     * value是string类型的redis操作类
     *
     * @author liaoxiting
     * @date 2021/1/31 20:45
     */
    @Bean
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return CreateRedisTemplateUtil.createString(redisConnectionFactory);

    }

    /**
     * 创建默认的value是string类型的redis缓存
     *
     * @author liaoxiting
     * @date 2021/1/31 20:39
     */
    @Bean
    public DefaultStringRedisCacheOperator defaultStringCacheOperator(RedisTemplate<String, String> stringRedisTemplate) {
        return new DefaultStringRedisCacheOperator(stringRedisTemplate);
    }

    /**
     * 创建默认的value是object类型的redis缓存
     *
     * @author liaoxiting
     * @date 2021/1/31 20:39
     */
    @Bean
    public DefaultRedisCacheOperator defaultRedisCacheOperator(RedisTemplate<String, Object> objectRedisTemplate) {
        return new DefaultRedisCacheOperator(objectRedisTemplate);
    }

}
