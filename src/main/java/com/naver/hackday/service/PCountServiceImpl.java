package com.naver.hackday.service;

import com.naver.hackday.model.PCount;
import com.naver.hackday.util.ProcessRedisData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PCountServiceImpl implements PCountService {

  @Autowired
  private CommentService commentService;

  @Autowired
  private ProcessRedisData processRedisData;

  // @Async
  @Override
  public void updatePCount() {
    List<PCount> pCountList = processRedisData.getResultList();

    if (!pCountList.isEmpty())
      commentService.updatePCount(pCountList);
  }

}
