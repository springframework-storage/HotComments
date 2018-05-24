package com.naver.hackday.repository;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.PCount;

import java.util.List;

public interface CommentRepository {

  CommentDto findById(int id);
  void updatePCount(List<PCount> pCountList);

}
