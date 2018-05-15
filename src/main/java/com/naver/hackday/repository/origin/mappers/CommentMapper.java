package com.naver.hackday.repository.origin.mappers;

import com.naver.hackday.dto.CommentDto;

public interface CommentMapper {

   CommentDto findById(int commentId);

}
