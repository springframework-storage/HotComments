package com.naver.hackday.repository;

import com.naver.hackday.model.ReactParam;

public interface CheckToReactRepository {

  void checkToReact(int postId, int commentId, int userId, ReactParam reactParam);

}
