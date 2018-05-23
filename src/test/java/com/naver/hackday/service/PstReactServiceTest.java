package com.naver.hackday.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PstReactServiceTest {

  private static final String KEY = "Pst";

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void pstReact() throws Exception {

    redisTemplate.opsForSet().add(KEY + 1, 1);
    redisTemplate.opsForSet().add(KEY + 2, 2);
    redisTemplate.opsForSet().remove(KEY + 2, 2);
    log.info("Pst1's members : {}", redisTemplate.opsForSet().members(KEY + 1));
    log.info("Pst2's members : {}", redisTemplate.opsForSet().members(KEY + 2));

    Boolean flag1 = redisTemplate.opsForSet().isMember(KEY + 1, 1);
    Boolean flag2 = redisTemplate.opsForSet().isMember(KEY + 2, 2);
    Assert.assertEquals(true, flag1);
    Assert.assertEquals(false, flag2);

  }

}
