package com.naver.hackday.service;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.PCount;
import com.naver.hackday.repository.CommentRepository;
import com.naver.hackday.repository.origin.mappers.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements CommentRepository {

  @Autowired
  private CommentMapper commentMapper;

  @Override
  public CommentDto findById(int commentId) {
    return commentMapper.findById(commentId);
  }

  @Override
  public void updatePCount(List<PCount> pCountList) {
    commentMapper.updatePCount(pCountList);
  }

}
