package com.naver.hackday.repository.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisRepositoryImpl(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long setListToListRight(String key, Object data) {
        return redisTemplate.opsForList().rightPushAll(key, data);
    }

    @Override
    public List<Object> getRangeFromList(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void setData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    @Override
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setExpiredTimeMillisec(String key, Long expiredTime) {
        redisTemplate.expire(key, expiredTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public Long getExpiredTimeMillisec(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public Long addMemberToSet(String key, Object data) {
        return redisTemplate.opsForSet().add(key, data);
    }

    @Override
    public Boolean isMember(String key, Object data) {
        return redisTemplate.opsForSet().isMember(key, data);
    }
}
