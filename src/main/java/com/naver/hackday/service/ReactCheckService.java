package com.naver.hackday.service;

import com.naver.hackday.repository.ReactCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactCheckService implements ReactCheckRepository {

  @Autowired
  private PstReactService pstReactService;

  @Override
  public void reactCheck(int postId, int commentId, int userId, int reactValue) {

    if (reactValue == 1) {
      pstReactService.pstReact(postId, commentId, userId);
    }
    else {
      // 비공감의 경우를 위해 남겨 놓습니다.
    }

  }

}
