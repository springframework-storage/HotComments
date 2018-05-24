package com.naver.hackday.util;

import com.naver.hackday.model.PCount;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProcessRedisDataTest {

  @Autowired
  private RedisTemplate redisTemplate;

  private List<Integer> increaseList = new ArrayList<Integer>();  // 공감 List
  private List<Integer> decreaseList = new ArrayList<Integer>();  // 공감 취소 List
  private List<PCount> resultList = new ArrayList<PCount>();

  @Test
  public void fillIncreaseDecreaseList() throws Exception {

    redisTemplate.opsForList().rightPush("KEY", 1);
    redisTemplate.opsForList().rightPush("KEY", 2);
    redisTemplate.opsForList().rightPush("KEY", -3);
    redisTemplate.opsForList().rightPush("KEY", -4);

    Object commentId = null;
    for (int i = 0; i < 100; ++i) {
      commentId = this.redisTemplate.opsForList().leftPop("KEY");
      if (commentId == null) break;
      if ((int) commentId > 0) increaseList.add((int) commentId);
      else decreaseList.add(Math.abs((int) commentId));
    }

    log.info("increaseList: {}", increaseList.toString());
    log.info("decreaseList: {}", decreaseList.toString());

  }

  @Test
  public void arrangeIncreaseDecreaseList() throws Exception {
    increaseList.add(5); increaseList.add(2); increaseList.add(3);
    increaseList.add(2); increaseList.add(3); increaseList.add(7);
    decreaseList.add(5); decreaseList.add(3); decreaseList.add(2);
    decreaseList.add(2); decreaseList.add(7); decreaseList.add(1);
    // 5 2 3 2 3 7 -> 2 2 3 3 5 7
    // 5 3 2 2 7 1 -> 1 2 2 3 5 7

    Collections.sort(increaseList);
    Collections.sort(decreaseList);
    log.info("increaseList: {}", increaseList.toString());
    log.info("decreaseList: {}", decreaseList.toString());

    if (!increaseList.isEmpty() && !decreaseList.isEmpty()) {
      int increaseIndex = 0;
      int decreaseIndex = 0;

      while (increaseIndex < increaseList.size() && decreaseIndex < decreaseList.size()) {
        if (increaseList.get(increaseIndex) == decreaseList.get(decreaseIndex)) {
          increaseList.remove(increaseIndex);
          decreaseList.remove(decreaseIndex);
        }
        else if (increaseList.get(increaseIndex) < decreaseList.get(decreaseIndex)) increaseIndex++;
        else decreaseIndex++;
      }
    }
    log.info("increaseList: {}", increaseList.toString());
    log.info("decreaseList: {}", decreaseList.toString());
  }

  @Test
  public void totalIncreaseCount() throws Exception {
    increaseList.add(1); increaseList.add(1); increaseList.add(1);
    increaseList.add(2); increaseList.add(3); increaseList.add(3);

    int count = 0;
    int commentId = increaseList.get(0);
    for (int index = 0; index < increaseList.size(); ++index) {

      if (commentId == increaseList.get(index)) count++;
      else {
        resultList.add(new PCount(commentId, count));
        commentId = increaseList.get(index);
        count = 1;
      }
    }
    resultList.add(new PCount(commentId, count));

    Assert.assertEquals(resultList.get(0).getCount(), 3);
    Assert.assertEquals(resultList.get(1).getCount(), 1);
    Assert.assertEquals(resultList.get(2).getCount(), 2);

    log.info("resultList: {}", resultList.size());
    log.info("index 0: {}, {}", resultList.get(0).getId(), resultList.get(0).getCount());
    log.info("index 1: {}, {}", resultList.get(1).getId(), resultList.get(1).getCount());
    log.info("index 2: {}, {}", resultList.get(2).getId(), resultList.get(2).getCount());
  }

}
