package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.service.CommentOriginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CommentControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private CommentOriginServiceImpl commentOriginService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void getCommentList() throws Exception {
    this.mockMvc.perform(get("/v1/comments/1")
<<<<<<< HEAD
                .param("userId", "1")
                .param("cursor", "1")
                .param("pageSize", "100")
                .param("orderType", "ASC")
                .param("pageNo", "1"))
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("doGet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
=======
            .param("userId", "1")
            .param("cursor", "1")
            .param("pageSize", "100")
            .param("orderType", "ASC")
            .param("pageNo", "1"))
            .andExpect(handler().handlerType(CommentController.class))
            .andExpect(handler().methodName("doGet"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
>>>>>>> temp/develop

    // 위 코드와 별개 - 간이 테스트
    BaseResponse<BaseListRtn<CommentDto>> result = this.commentOriginService.doGet(1, 1, 100, "ASC", 1, 1);
    assertThat(result).isNotNull();
  }

<<<<<<< HEAD
}
=======
}
>>>>>>> temp/develop
