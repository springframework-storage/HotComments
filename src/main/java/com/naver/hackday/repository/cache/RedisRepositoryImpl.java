package com.naver.hackday.repository.cache;

import com.naver.hackday.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static Logger logger = LoggerFactory.getLogger("log.hackday");

    private RedisTemplate<String, Object> redisTemplate;
    private RedisTemplate<String, CommentDto> commentDtoRedisTemplate;

    @Autowired
    public RedisRepositoryImpl(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
                               @Qualifier("commentDtoRedisTemplate") RedisTemplate<String, CommentDto> commentDtoRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.commentDtoRedisTemplate = commentDtoRedisTemplate;
    }

    @Override
    public Long setListToListRight(String key, Object data, Long expiredTime) {
        Long rowCount = redisTemplate.opsForList().rightPush(key, data);
        setExpiredTimeMillisec(key, expiredTime);
        return rowCount;
    }

    @Override
    public List<Object> getRangeFromList(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public List<CommentDto> getRangeFromListForCommentDto(String key, long start, long end) {
        return commentDtoRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void setData(String key, Object data, Long expiredTime) {
        redisTemplate.opsForValue().set(key, data);
        setExpiredTimeMillisec(key, expiredTime);
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
    public Object getDataOfHashKey(String key, String HashKey) {
        return redisTemplate.opsForHash().get(key, HashKey);
    }

    @Override
    public boolean hasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public void addDataOfHashKey(String key, String HashKey, Object data, Long expiredTime) {
        redisTemplate.opsForHash().put(key, HashKey, data);
        setExpiredTimeMillisec(key, expiredTime);
    }

    @Override
    public void deleteData(String key) {
        logger.debug("delete redis data key : " + key);
        redisTemplate.delete(key);
    }

    @Override
    public boolean isMember(String key, int member) {
        Boolean isMemberUser = redisTemplate.opsForSet().isMember(key, member);
        if (isMemberUser == null) {
            return false;
        }
        return isMemberUser;
    }

}
