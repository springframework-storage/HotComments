package com.naver.hackday.service;

import com.naver.hackday.repository.ReactCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactCheckService implements ReactCheckRepository {

  @Autowired
  private RedisPstReactService redisPstReactService;

  @Override
  public void reactCheck(int postId, int commentId, int userId, String react) {

    if (react.equals("pst")) {
      redisPstReactService.pstReact(postId, commentId, userId);
    }
    else {
      try {
        throw new IllegalAccessException("잘못된 요청입니다.");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

  }

}
