package com.naver.hackday.step;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.naver.hackday.repository.origin.mappers.CommentMapper;

@Component
@Scope("prototype")
public class IncreaseWriter implements ItemWriter<Integer> {

	@Autowired
	private CommentMapper commentMapper;

	//공감 요청된 댓글들 마다 MySQL에서 공감 수치를 증가시키는 Mapper호출
	@Override
	public void write(List<? extends Integer> commentIdList) throws Exception {
		commentMapper.updateIncreasePCount(commentIdList); //공감요청 된 댓글 id 리스트 파라미터와 함께 Mapper호출
	}
}
