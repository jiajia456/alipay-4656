package com.ali.pay.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用StringRedisSerializer作为键的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 使用GenericJackson2JsonRedisSerializer作为值的序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 使用StringRedisSerializer作为哈希键的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());

        // 使用GenericJackson2JsonRedisSerializer作为哈希值的序列化器
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}

