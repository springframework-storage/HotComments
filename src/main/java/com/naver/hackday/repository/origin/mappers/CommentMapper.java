package com.naver.hackday.repository.origin.mappers;

import com.naver.hackday.dto.CommentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {

    List<CommentDto> retreiveCommentOrderByLikeCount(@Param("cursor") Integer cursor, @Param("size") Integer size);

}
