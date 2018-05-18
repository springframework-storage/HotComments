package com.naver.hackday.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.naver.hackday.model.PCount;
import com.naver.hackday.repository.origin.mappers.CommentMapper;
import com.naver.hackday.util.ProcessRedisData;

@Service
public class PCountServiceImpl implements PCountService {

	@Autowired
	CommentMapper commentMapper;

	@Autowired
	private ProcessRedisData processRedisData;

	private List<PCount> pCountList = new ArrayList<PCount>();

	@Override
	@Async
	public void updatePCount() {
		pCountList = processRedisData.getResultList();
		if (!pCountList.isEmpty())
			commentMapper.updatePCount(pCountList);
	}

}
