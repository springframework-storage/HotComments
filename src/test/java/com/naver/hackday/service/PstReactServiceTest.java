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
  private static final String SCHEDULER = "Scheduler";

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void pstReact() throws Exception {

    this.redisTemplate.opsForSet().add(KEY + 1, 1);
    this.redisTemplate.opsForSet().add(KEY + 2, 2);
    this.redisTemplate.opsForSet().remove(KEY + 2, 2);
    log.info("Pst1's members : {}", this.redisTemplate.opsForSet().members(KEY + 1));
    log.info("Pst2's members : {}", this.redisTemplate.opsForSet().members(KEY + 2));

    Boolean flag1 = this.redisTemplate.opsForSet().isMember(KEY + 1, 1);
    Boolean flag2 = this.redisTemplate.opsForSet().isMember(KEY + 2, 2);
    Assert.assertEquals(true, flag1);
    Assert.assertEquals(false, flag2);

  }

  @Test
  public void schdulerModulator() throws Exception {

    int commentId = -2;
    int mod = Math.abs(commentId) % 4;
    log.info("mod: {}", mod);

    switch (mod) {
      case 1: this.redisTemplate.opsForList().rightPush(SCHEDULER + 1, commentId); break;
      case 2: this.redisTemplate.opsForList().rightPush(SCHEDULER + 2, commentId); break;
      case 3: this.redisTemplate.opsForList().rightPush(SCHEDULER + 3, commentId); break;
      case 0: this.redisTemplate.opsForList().rightPush(SCHEDULER + 4, commentId); break;
    }

    log.info("Scheduler1 leftPop: {}", this.redisTemplate.opsForList().leftPop(SCHEDULER + 1));
    log.info("Scheduler2 leftPop: {}", this.redisTemplate.opsForList().leftPop(SCHEDULER + 2));
    log.info("Scheduler3 leftPop: {}", this.redisTemplate.opsForList().leftPop(SCHEDULER + 3));
    log.info("Scheduler4 leftPop: {}", this.redisTemplate.opsForList().leftPop(SCHEDULER + 4));

  }

}
