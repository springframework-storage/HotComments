package com.naver.hackday.repository.origin;


import com.naver.hackday.dto.CommentDto;

import java.util.List;

public interface CommentRepository {

    List<CommentDto> retreiveCommentOrderByLikeCount(int cursor, int size);

}
