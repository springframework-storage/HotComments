package com.naver.hackday.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.naver.hackday.repository.origin.mappers.CommentMapper;
import com.naver.hackday.util.ReadRedisData;

@Service
public class PCountServiceImpl implements PCountService{
	
	@Autowired
	CommentMapper commentMapper;
	
	@Autowired
	private ReadRedisData readRedisData;
	
	private List<Integer> commentIdList = new ArrayList<Integer>();

	@Override
	@Async
	public void increasePCount() {
		commentIdList = readRedisData.readIncreaseData();
		if(!commentIdList.isEmpty())
			commentMapper.increasePCount(commentIdList);
	}

	@Override
	@Async
	public void decreasePCount() {
		commentIdList = readRedisData.readDecreaseData();
		if(!commentIdList.isEmpty())
			commentMapper.decreasePCount(commentIdList);		
	}

}
