package com.naver.hackday.repository;

import com.naver.hackday.dto.CommentDto;

public interface CommentRepository {

  CommentDto findById(int id);

}
