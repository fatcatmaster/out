package com.fatcat.core.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * AutoConfigureBefore 只对外部依赖配置有效，即 spring.factories 中的配置有效；可以指定实例化 bean 的顺序
 * RedisAutoConfiguration 会加载 redisTemplate，所以为了避免产生 bean 覆盖的错误，需要指定当前 bean 优先实例化
 * AutoConfigureBefore、AutoConfigureAfter、AutoConfigureOrder 都可指定顺序
 *
 * @author fatcat
 * @description redis 的全局配置
 * @create 2021/5/7
 **/
@SpringBootConfiguration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfiguration {

    @Resource(name = "objectMapper")
    private ObjectMapper objectMapper;

    /**
     * 指定redis序列化保存数据的方式
     * GenericJackson2JsonRedisSerializer 可以序列化和反序列化泛型数据，性能差
     * Jackson2JsonRedisSerializer 不可反序列化泛型数据，譬如List<Object>，性能高
     * 用户主动以json格式保存，可忽略上述方案差异; 也可以指定 ObjectMapper，使用全局 json 序列化和反序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 指定java对象可序列化的内容范围；PropertyAccessor.ALL表示当前指定为所有（字段，get\set 方法，构造器等）；JsonAutoDetect.Visibility.ANY表示修饰符为所有（public\private\protected）
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // 使用全局序列化方式
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 其他默认
        template.afterPropertiesSet();
        return template;
    }
}
