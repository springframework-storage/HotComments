package com.naver.hackday.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

@Component
public class ReadRedisData {

	@Resource(name = "redisTemplate")
	private ListOperations<String, Integer> listOperations;

	private List<Integer> commentIdList;

	public List<Integer> readIncreaseData() {
		commentIdList = new ArrayList<Integer>();
		Integer commentId;

		for (int i = 0; i < 3000; ++i) {
			commentId = listOperations.leftPop("PstInsert");
			if (commentId != null)
				commentIdList.add(commentId);
			else
				return commentIdList;
		}
		return commentIdList;
	}
	
	public List<Integer> readDecreaseData() {
		commentIdList = new ArrayList<Integer>();
		Integer commentId;

		for (int i = 0; i < 3000; ++i) {
			commentId = listOperations.leftPop("PstDelete");
			if (commentId != null)
				commentIdList.add(commentId);
			else
				return commentIdList;
		}
		return commentIdList;
	}
}
