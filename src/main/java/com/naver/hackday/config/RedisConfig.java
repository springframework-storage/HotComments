package com.naver.hackday.config;

import com.naver.hackday.dto.CommentDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class RedisConfig {

  private JedisPoolConfig createJedisPoolConfig(final Properties redisConfig) {
    final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    // set as default
    jedisPoolConfig.setTestOnBorrow(false);
    jedisPoolConfig.setTestOnReturn(false);
    jedisPoolConfig.setTestWhileIdle(true);
    jedisPoolConfig.setNumTestsPerEvictionRun(10);
    jedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000);
    jedisPoolConfig.setMinIdle(1);

    // set by config
    jedisPoolConfig.setMaxWaitMillis(Long.parseLong(redisConfig.getProperty("redis.pool.maxWaitMillis")));
    jedisPoolConfig.setMaxIdle(Integer.parseInt(redisConfig.getProperty("redis.pool.maxIdle")));
    jedisPoolConfig.setMaxTotal(Integer.parseInt(redisConfig.getProperty("redis.pool.maxTotal")));

    return jedisPoolConfig;
  }

  @Bean(name = "jedisConnectionFactory", destroyMethod = "destroy")
  public JedisConnectionFactory jedisConnectionFactory() throws IOException {
    final Properties redisConfig = PropertiesLoaderUtils.loadProperties(
            new PathMatchingResourcePatternResolver().getResource("classpath:redis.properties"));

    final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

    jedisConnectionFactory.setHostName(redisConfig.getProperty("redis.hostname"));
    jedisConnectionFactory.setPort(Integer.parseInt(redisConfig.getProperty("redis.port")));
    jedisConnectionFactory.setUsePool(Boolean.parseBoolean(redisConfig.getProperty("redis.pool.use")));
    jedisConnectionFactory.setTimeout(Integer.parseInt(redisConfig.getProperty("redis.timeout")));
    jedisConnectionFactory.setPoolConfig(createJedisPoolConfig(redisConfig));

    jedisConnectionFactory.afterPropertiesSet();

    return jedisConnectionFactory;
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate() throws IOException {
    final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setEnableTransactionSupport(true);
    redisTemplate.setConnectionFactory(jedisConnectionFactory());

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

  @Bean(name = "commentDtoRedisTemplate")
  public RedisTemplate<String, CommentDto> commentDtoRedisTemplate() throws IOException {
    final RedisTemplate<String, CommentDto> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(CommentDto.class));

    return redisTemplate;
  }

  @Bean(name = "stringRedisTemplate")
  public RedisTemplate<String, String> stringRedisTemplate() throws IOException {
    final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

}