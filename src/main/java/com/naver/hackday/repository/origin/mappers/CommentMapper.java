package com.naver.hackday.repository.origin.mappers;

import java.util.List;

public interface CommentMapper {
	//각 댓글의 pCount를 증가시킴
	void increasePCount(List<? extends Integer> commentIdList);
	void decreasePCount(List<? extends Integer> commentIdList);
}