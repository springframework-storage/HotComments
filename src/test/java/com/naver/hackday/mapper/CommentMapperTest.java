package com.naver.hackday.mapper;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.PCount;
import com.naver.hackday.repository.origin.mappers.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CommentMapperTest {

  @Autowired
  private CommentMapper commentMapper;

  @Test
  public void updatePCount() throws Exception {

    Assert.assertNotNull(commentMapper);

    CommentDto commentDto1 = commentMapper.findById(1);
    CommentDto commentDto2 = commentMapper.findById(2);
    Assert.assertNotNull(commentDto1);
    Assert.assertNotNull(commentDto2);
    log.info("CommentDto Id:1 - {}", commentDto1);
    log.info("CommentDto Id:2 - {}", commentDto2);

    PCount pCount1 = new PCount(1, 10);
    PCount pCount2 = new PCount(2, 100);
    List<PCount> pCountList = new ArrayList<>();
    pCountList.add(pCount1);
    pCountList.add(pCount2);
    Assert.assertNotNull(pCountList);
    log.info("pCountList Size: {}", pCountList.size());
    log.info("pCountList.get(0): id-{}, count-{}", pCountList.get(0).getId(), pCountList.get(0).getCount());
    log.info("pCountList.get(1): id-{}, count-{}", pCountList.get(1).getId(), pCountList.get(1).getCount());

    commentMapper.updatePCount(pCountList);
    log.info("CommentDto Id:1 - {}", commentMapper.findById(1));
    log.info("CommentDto Id:2 - {}", commentMapper.findById(2));

  }

}
