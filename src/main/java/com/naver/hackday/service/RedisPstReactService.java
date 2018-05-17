package com.naver.hackday.service;

import com.naver.hackday.repository.PstReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class RedisPstReactService implements PstReactRepository {

  private static final String KEY = "Pst";
  private RedisTemplate<String, Integer> redisTemplate;
  private RedisTemplate<String, String> stringRedisTemplate;

  // 댓글에 공감한 사용자 목록을 저장할 Set
  private SetOperations<String, Integer> usersByCommentIdSet;

  // 해당 사용자가 공감한 댓글의 목록을 저장할 Set
  private SetOperations<String, Integer> commentsByUserIdSet;

  // batch에서 사용할 List
  private ListOperations<String, Integer> listOperations;

  // 가장 최근 공감 시간 저장을 위한 ValueOperations
  private ValueOperations<String, String> recentlyReactTime;

  @Autowired
  private RedisPstReactService(RedisTemplate redisTemplate, RedisTemplate stringRedisTemplate) {
    this.redisTemplate = redisTemplate;
    this.stringRedisTemplate = stringRedisTemplate;
  }

  /*
  객체의 초기화
  객체가 생성된 후 별도의 초기화 작업을 위해 실행하는 메소드
  init 메소드는 WAS가 띄워질 때 실행됩니다.
  (Autowired가 먼저 된 후 init 메소드가 실행됩니다.)
   */
  @PostConstruct
  private void init() {
    usersByCommentIdSet = redisTemplate.opsForSet();        // "Pst" + "commentId" : userId
    commentsByUserIdSet = redisTemplate.opsForSet();        // "userId" + "Pst" : commentId
    listOperations = redisTemplate.opsForList();
    recentlyReactTime = stringRedisTemplate.opsForValue();  // "PstTime" : Long.toString(new Date().getTime())
  }

  // 사용자의 공감 요청 처리
  @Override
  public void pstReact(int postId, int commentId, int userId) {
    // 이미 해당 댓글에 공감했다면 공감 취소
    if (this.isMember(commentId, userId)) {
      this.delete(commentId, userId);
    }
    // 아니라면 공감
    else {
      this.insert(postId, commentId, userId);
    }
  }

  // Redis에 공감 데이터 삽입
  @Override
  public void insert(int postId, int commentId, int userId) {

    this.usersByCommentIdSet.add(KEY + Integer.toString(commentId), userId);
    this.commentsByUserIdSet.add(Integer.toString(userId) + KEY, commentId);
    this.listOperations.rightPush(KEY + "Insert", commentId);
    this.recentlyReactTime.set(KEY + "Time", Long.toString(new Date().getTime()));

  }

  // Redis에서 공감 데이터 삭제
  @Override
  public void delete(int commentId, int userId) {

    this.usersByCommentIdSet.remove(KEY + Integer.toString(commentId), userId);
    this.commentsByUserIdSet.remove(Integer.toString(userId) + KEY, commentId);
    this.listOperations.rightPush(KEY + "Delete", commentId);
    this.recentlyReactTime.set(KEY + "Time", Long.toString(new Date().getTime()));

  }

  // 공감 여부 확인을 위한 메소드
  public boolean isMember(int commentId, int userId) {
    return this.usersByCommentIdSet.isMember(KEY + Integer.toString(commentId), userId);
  }

}
