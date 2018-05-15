package com.naver.hackday.repository;

public interface PstReactRepository {

  void pstReact(int postId, int commentId, int userId);
  void insert(int postId, int commentId, int userId);
  void delete(int commentId, int userId);

}
