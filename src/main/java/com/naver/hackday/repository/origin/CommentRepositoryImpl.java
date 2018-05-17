package com.naver.hackday.repository.origin;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.repository.origin.mappers.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> retreiveCommentOrderByLikeCount(int cursor, int size) {
        return commentMapper.retreiveCommentOrderByLikeCount(cursor, size);
    }
}
