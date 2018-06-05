package com.naver.hackday.util;

import com.naver.hackday.model.PCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ProcessRedisData {

  private static final String SCHEDULER = "Scheduler";
  private static final int MAX_FILL = 3000;

  private RedisTemplate<String, Integer> redisTemplate;
  private ListOperations<String, Integer> listOperations;

  private List<Integer> increaseList = new ArrayList<Integer>();  // 공감 List
  private List<Integer> decreaseList = new ArrayList<Integer>();  // 공감 취소 List
  private List<PCount> resultList = new ArrayList<PCount>();      // 최종적으로 사용될 List

  private LocalHostIdentifier localHostIdentifier = new LocalHostIdentifier();

  @Autowired
  private ProcessRedisData(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  private void init() {
    this.listOperations = this.redisTemplate.opsForList();
  }

  public List<PCount> getResultList() {
    this.getRedisData();

    Collections.sort(increaseList);
    Collections.sort(decreaseList);

    if (!increaseList.isEmpty() && !decreaseList.isEmpty()) {
      this.arrangeIncDecList(increaseList, decreaseList);
    }

    return this.makeResultList(increaseList, decreaseList);
  }

  /**
   * Redis 데이터를 꺼내는 것을 시작하는 메소드
   */
  private void getRedisData() {
    String key = null;

    switch (localHostIdentifier.getHostMapping()) {
      case 1:
        key = SCHEDULER + 1;
        break;
      case 2:
        key = SCHEDULER + 2;
        break;
      case 3:
        key = SCHEDULER + 3;
        break;
      case 4:
        key = SCHEDULER + 4;
        break;
    }
    this.fillIncDecList(key);
  }

  /**
   * Redis의 데이터를 꺼내 각각 채워진 공감, 공감취소 List의 요소들을 비교해 간소화합니다.
   *
   * @param increaseList
   * @param decreaseList
   */
  private void arrangeIncDecList(List<Integer> increaseList, List<Integer> decreaseList) {
    int increaseIndex = 0;
    int decreaseIndex = 0;
    int increaseListSize = increaseList.size();
    int decreaseListSize = decreaseList.size();

    while (increaseIndex < increaseListSize && decreaseIndex < decreaseListSize) {
      if (increaseList.get(increaseIndex) == decreaseList.get(decreaseIndex)) {
        increaseList.remove(increaseIndex);
        decreaseList.remove(decreaseIndex);
        increaseListSize--;
        decreaseListSize--;
      }
      else if (increaseList.get(increaseIndex) < decreaseList.get(decreaseIndex)) increaseIndex++;
      else decreaseIndex++;
    }

  }

  /**
   * 주기적인 데이터 저장을 위한 최종 List를 만듭니다.
   *
   * @param increaseList
   * @param decreaseList
   * @return
   */
  private List<PCount> makeResultList(List<Integer> increaseList, List<Integer> decreaseList) {

    if (increaseList.isEmpty() && decreaseList.isEmpty()) return null;
    else if (!increaseList.isEmpty() && decreaseList.isEmpty()) this.totalIncreaseCount(increaseList);
    else if (increaseList.isEmpty() && !decreaseList.isEmpty()) this.totalDecreaseCount(decreaseList);
    else {
      this.totalIncreaseCount(increaseList);
      this.totalDecreaseCount(decreaseList);
    }
    return resultList;

  }

  /**
   * Redis의 데이터를 꺼내 공감, 공감취소 List에 commentId를 채웁니다.
   *
   * @param key
   */
  private void fillIncDecList(String key) {
    Integer commentId = null;

    for (int i = 0; i < MAX_FILL; ++i) {
      commentId = this.listOperations.leftPop(key);

      if (commentId == null) break;
      if (commentId > 0) this.increaseList.add(commentId);
      else this.decreaseList.add(Math.abs(commentId));
    }
  }

  /**
   * 공감 List 데이터를 resultList에 추가합니다.
   *
   * @param increaseList
   */
  private void totalIncreaseCount(List<Integer> increaseList) {
    int count = 0;
    int commentId = increaseList.get(0);
    int increaseListSize = increaseList.size();

    for (int index = 0; index < increaseListSize; ++index) {

      if (commentId == increaseList.get(index)) count++;
      else {
        resultList.add(new PCount(commentId, count));
        commentId = increaseList.get(index);
        count = 1;
      }
    }
    resultList.add(new PCount(commentId, count));
  }

  /**
   * 공감 취소 List 데이터를 resultList에 추가합니다.
   *
   * @param decreaseList
   */
  private void totalDecreaseCount(List<Integer> decreaseList) {
    int count = 0;
    int commentId = decreaseList.get(0);
    int decreaseListSize = decreaseList.size();

    for (int index = 0; index < decreaseListSize; ++index) {

      if (commentId == decreaseList.get(index)) count++;
      else {
        resultList.add(new PCount(commentId, -count));
        commentId = decreaseList.get(index);
        count = 1;
      }
    }
    resultList.add(new PCount(commentId, -count));
  }

}
