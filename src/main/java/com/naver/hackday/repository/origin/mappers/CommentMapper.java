package com.naver.hackday.repository.origin.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.PCount;

public interface CommentMapper {
  
  List<CommentDto> retreiveCommentOrderByLikeCount(@Param("cursor") Integer cursor, @Param("size") Integer size);
    CommentDto findById(int commentId);
	void updatePCount(List<PCount> commentIdList);
  
}
