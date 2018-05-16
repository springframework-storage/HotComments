package com.naver.hackday.repository.origin.mappers;

import java.util.List;

public interface CommentMapper {
	//각 댓글의 pCount를 증가시킴
	void updateIncreasePCount(List<? extends Integer> commentIdList);
	void updateDecreasePCount(List<? extends Integer> commentIdList);
}