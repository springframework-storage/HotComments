package com.naver.hackday.service;

import com.naver.hackday.repository.PstReactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class PstReactService implements PstReactRepository {

  private static Logger logger = LoggerFactory.getLogger("log.hackday");

  private static final String KEY = "Pst";
  private static final String SCHEDULER = "Scheduler";
  private RedisTemplate<String, Integer> redisTemplate;
  private RedisTemplate<String, String> stringRedisTemplate;

  // 댓글에 공감한 사용자 목록을 저장할 Set
  private SetOperations<String, Integer> usersByCommentIdSet;

  // 해당 사용자가 공감한 댓글의 목록을 저장할 Set
  private SetOperations<String, Integer> commentsByUserIdSet;

  // 공감수를 MySQL에 업데이트할 스케줄러에서 사용할 List
  private ListOperations<String, Integer> listOperations;

  // 가장 최근 공감 시간 저장을 위한 ValueOperations
  private ValueOperations<String, String> recentlyReactTime;

  @Autowired
  private PstReactService(RedisTemplate redisTemplate, RedisTemplate stringRedisTemplate) {
    this.redisTemplate = redisTemplate;
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @PostConstruct
  private void init() {
    usersByCommentIdSet = redisTemplate.opsForSet();        // "Pst" + "commentId" : userId
    commentsByUserIdSet = redisTemplate.opsForSet();        // "userId" + "Pst" : commentId
    listOperations = redisTemplate.opsForList();
    recentlyReactTime = stringRedisTemplate.opsForValue();  // "PstTime" : Long.toString(new Date().getTime())
  }

  /**
   * 사용자의 공감 요청 처리
   *
   * @param postId
   * @param commentId
   * @param userId
   */
  @Override
  public void pstReact(int postId, int commentId, int userId) {
    // 이미 해당 댓글에 공감했다면 공감 취소
    if (this.isMember(commentId, userId)) {
      this.pstDelete(commentId, userId);
      logger.debug("공감 취소 : commentId : " + commentId + "userId : " + userId);
    }
    // 아니라면 공감
    else {
      this.pstInsert(postId, commentId, userId);
      logger.debug("공감 : commentId : " + commentId + "userId : " + userId);
    }
  }

  /**
   * Redis에 공감 데이터 삽입
   *
   * @param postId
   * @param commentId
   * @param userId
   */
  private void pstInsert(int postId, int commentId, int userId) {

    this.usersByCommentIdSet.add(KEY + Integer.toString(commentId), userId);
    this.commentsByUserIdSet.add(Integer.toString(userId) + KEY, commentId);
    this.recentlyReactTime.set(KEY + "Time", Long.toString(new Date().getTime()));
    this.schedulerModulator(commentId);

  }

  /**
   * Redis에서 공감 데이터 삭제
   *
   * @param commentId
   * @param userId
   */
  private void pstDelete(int commentId, int userId) {

    this.usersByCommentIdSet.remove(KEY + Integer.toString(commentId), userId);
    this.commentsByUserIdSet.remove(Integer.toString(userId) + KEY, commentId);
    this.recentlyReactTime.set(KEY + "Time", Long.toString(new Date().getTime()));
    this.schedulerModulator(commentId * (-1));

  }

  /**
   * 공감 여부 확인을 위한 메소드
   *
   * @param commentId
   * @param userId
   * @return
   */
  private boolean isMember(int commentId, int userId) {
    return this.usersByCommentIdSet.isMember(KEY + Integer.toString(commentId), userId);
  }

  /**
   * 스케줄러 4대 각각의 listOperation에 분배하는 메소드
   * commentId를 매개변수로 받아 나머지를 계산해 분배합니다.
   *
   * @param commentId
   */
  private void schedulerModulator(int commentId) {

    switch (Math.abs(commentId) % 4) {
      case 1: this.listOperations.rightPush(SCHEDULER + 1, commentId); break;
      case 2: this.listOperations.rightPush(SCHEDULER + 2, commentId); break;
      case 3: this.listOperations.rightPush(SCHEDULER + 3, commentId); break;
      case 0: this.listOperations.rightPush(SCHEDULER + 4, commentId); break;
    }

//    if (Math.abs(commentId) % 4 == 1) this.listOperations.rightPush(SCHEDULER + 1, commentId);
//    else if (Math.abs(commentId) % 4 == 2) this.listOperations.rightPush(SCHEDULER + 2, commentId);
//    else if (Math.abs(commentId) % 4 == 3) this.listOperations.rightPush(SCHEDULER + 3, commentId);
//    else this.listOperations.rightPush(SCHEDULER + 4, commentId);
  }

}
