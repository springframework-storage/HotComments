package com.naver.hackday.service;


import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;

public interface CommentListService {

    BaseResponse<BaseListRtn<CommentDto>> doGet(int cursor, int size, int pageNo, String orderType, int postId, int userId);

}
