package com.naver.hackday.repository.origin.mappers;

import java.util.List;

import com.naver.hackday.model.PCount;

public interface CommentMapper {
	void updatePCount(List<PCount> commentIdList);
}