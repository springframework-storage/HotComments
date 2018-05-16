package com.naver.hackday.step;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DecreaseReader implements ItemReader<Integer> {
	
	@Resource(name = "redisTemplate")
	private ListOperations<String, Integer> listOperations;
	
	//Redis에서 공감 요청 취소 된 댓글 번호를 차례로 읽음
	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Integer commentId = listOperations.leftPop("PstDelete");
		return (commentId == null ? null : commentId); //Redis의 List가 비어 있으면 null을 return 후 Job을 종료
	}

}

