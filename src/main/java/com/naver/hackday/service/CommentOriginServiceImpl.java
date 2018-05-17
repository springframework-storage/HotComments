package com.naver.hackday.service;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.dto.CommentRtn;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.repository.origin.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentOriginServiceImpl implements CommentListService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentOriginServiceImpl(@Qualifier("commentRepositoryImpl") CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public BaseResponse<BaseListRtn<CommentDto>> doGet(int cursor, int pageSize, int pageNo, String orderType, int postId, int userId) {
        BaseResponse<BaseListRtn<CommentDto>> result = new BaseResponse<>();
        BaseListRtn<CommentDto> baseListRtn = new BaseListRtn<>();

        List<CommentDto> dtos = commentRepository.retreiveCommentOrderByLikeCount((pageNo - 1) * pageSize, pageSize);

        baseListRtn.setDatas(dtos);

        result.setReturnCode(BaseCode.SUCCESS.getCode());
        result.setReturnMessage(BaseCode.SUCCESS.getMessage());
        result.setResult(baseListRtn);

        return result;
    }

}
