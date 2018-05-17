package com.naver.hackday.service;


import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;

public interface CommentListService<T> {

    BaseResponse<BaseListRtn<T>> doGet(int cursor, int pageSize, int pageNo, String orderType, int postId, int userId);

}
