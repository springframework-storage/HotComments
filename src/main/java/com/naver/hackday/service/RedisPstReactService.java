package com.naver.hackday.service;

import com.naver.hackday.repository.PstReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedisPstReactService implements PstReactRepository {

  private static final String KEY = "Pst";
  private RedisTemplate<String, Integer> redisTemplate;
  private SetOperations<String, Integer> setOperations;

  @Autowired
  private RedisPstReactService(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /*
  객체의 초기화
  객체가 생성된 후 별도의 초기화 작업을 위해 실행하는 메소드
  init 메소드는 WAS가 띄워질 때 실행됩니다.
  (Autowired가 먼저 된 후 init 메소드가 실행됩니다.)
   */
  @PostConstruct
  private void init() {
    setOperations = redisTemplate.opsForSet();
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
    this.setOperations.add(KEY + Integer.toString(commentId), userId);
  }

  // Redis에서 공감 데이터 삭제
  @Override
  public void delete(int commentId, int userId) {
    this.setOperations.remove(KEY + Integer.toString(commentId), userId);
  }

  // 공감 여부 확인을 위한 메소드
  public boolean isMember(int commentId, int userId) {
    return this.setOperations.isMember(KEY + Integer.toString(commentId), userId);
  }

}
