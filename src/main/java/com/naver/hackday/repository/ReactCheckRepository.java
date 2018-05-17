package com.naver.hackday.repository;

public interface ReactCheckRepository {

  void reactCheck(int postId, int commentId, int userId, int reactValue);

}
