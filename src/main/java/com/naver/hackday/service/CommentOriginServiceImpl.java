package com.naver.hackday.service;

import com.naver.hackday.dto.CommentDto;
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
    public BaseResponse<BaseListRtn<CommentDto>> doGet(int cursor, int size, int pageNo, String orderType, int postId, int userId) {
        BaseResponse<BaseListRtn<CommentDto>> result = new BaseResponse<>();
        BaseListRtn<CommentDto> baseListRtn = new BaseListRtn<>();

        //캐싱 히트율을 높이기위해 페이지별로 데이터를 (size * 5) 개씩 가져옴.
        List<CommentDto> dtos = commentRepository.retreiveCommentOrderByLikeCount((pageNo - 1) * size, size);

        baseListRtn.setDatas(dtos);

        result.setReturnCode(BaseCode.SUCCESS.getCode());
        result.setReturnMessage(BaseCode.SUCCESS.getMessage());
        result.setResult(baseListRtn);

        return result;
    }

}
