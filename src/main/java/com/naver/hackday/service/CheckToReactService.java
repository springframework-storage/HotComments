package com.naver.hackday.service;

import com.naver.hackday.model.ReactParam;
import com.naver.hackday.repository.CheckToReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckToReactService implements CheckToReactRepository {

  private static final String PST = "Pst";

  @Autowired
  private PstReactService pstReactService;

  @Override
  public void checkToReact(int postId, int commentId, int userId, ReactParam reactParam) {

    if (reactParam.Pst.equals(reactParam)) {
      pstReactService.pstReact(postId, commentId, userId);
    }
    else {
      // 비공감의 경우를 위해 남겨 놓습니다.
    }

  }

}
