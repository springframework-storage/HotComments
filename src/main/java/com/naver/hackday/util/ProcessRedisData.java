package com.naver.hackday.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import com.naver.hackday.model.PCount;

@Component
public class ProcessRedisData {

	@Resource(name = "redisTemplate")
	private ListOperations<String, Integer> listOperations;

	private List<Integer> increaseList;

	private List<Integer> decreaseList;

	private List<PCount> resultList;

	public List<PCount> getResultList() {
		getRedisData();

		if (!increaseList.isEmpty()) 
			sortList(increaseList);
		
		if (!decreaseList.isEmpty())
			sortList(decreaseList);
		
		if (!increaseList.isEmpty() && !decreaseList.isEmpty())
			offset(increaseList, decreaseList);

		makeResultList(increaseList, decreaseList);

		return resultList;
	}

	public void getRedisData() {
		increaseList = new ArrayList<Integer>();
		decreaseList = new ArrayList<Integer>();

		Integer commentId;

		for (int i = 0; i < 3000; ++i) {
			commentId = listOperations.leftPop("PstDelete");
			if (commentId != null) {
				if (commentId > 0)
					increaseList.add(commentId);
				else
					decreaseList.add(Math.abs(commentId));
			} else
				return;
		}

	}

	public void sortList(List<Integer> arrayList) {
		Collections.sort(arrayList);
	}

	public void offset(List<Integer> increaseList, List<Integer> decreaseList) {
		int increaseIndex = 0;
		int decreaseIndex = 0;

		while (increaseIndex < increaseList.size() && decreaseIndex < decreaseList.size()) {
			if (increaseList.get(increaseIndex) == decreaseList.get(decreaseIndex)) {
				increaseList.remove(increaseIndex);
				decreaseList.remove(decreaseIndex);
				
				increaseIndex++;
				decreaseIndex++;
			} else if (increaseList.get(increaseIndex) < decreaseList.get(decreaseIndex))
				increaseIndex++;
			else
				decreaseIndex++;
		}

	}

	public void makeResultList(List<Integer> increaseList, List<Integer> decreaseList) {
		resultList = new ArrayList<PCount>();

		if (!increaseList.isEmpty() && !decreaseList.isEmpty()) {
			int count = 0;
			int commentId = increaseList.get(0);

			for (int index = 0; index < increaseList.size(); ++index) {
				if (commentId == increaseList.get(index))
					count++;
				else {
					resultList.add(new PCount(commentId, count));
					commentId = increaseList.get(index);
					count = 1;
				}
			}

			resultList.add(new PCount(commentId, count));

			count = 0;
			commentId = decreaseList.get(0);

			for (int index = 0; index < decreaseList.size(); ++index) {
				if (commentId == decreaseList.get(index))
					count++;
				else {
					resultList.add(new PCount(commentId, -count));
					commentId = decreaseList.get(index);
					count = 1;
				}
			}

			resultList.add(new PCount(commentId, -count));

		} else if (!increaseList.isEmpty()) {
			int count = 0;
			int commentId = increaseList.get(0);

			for (int index = 0; index < increaseList.size(); ++index) {
				if (commentId == increaseList.get(index))
					count++;
				else {
					resultList.add(new PCount(commentId, count));
					commentId = increaseList.get(index);
					count = 1;
				}
			}
			resultList.add(new PCount(commentId, count));

		} else if (!decreaseList.isEmpty()) {
			int count = 0;
			int commentId = decreaseList.get(0);

			for (int index = 0; index < decreaseList.size(); ++index) {
				if (commentId == decreaseList.get(index))
					count++;
				else {
					resultList.add(new PCount(commentId, -count));
					commentId = decreaseList.get(index);
					count = 1;
				}
			}
			resultList.add(new PCount(commentId, -count));
		}
	}
}
