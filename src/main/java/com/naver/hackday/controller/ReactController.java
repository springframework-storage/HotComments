package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.DefaultResponse;
import com.naver.hackday.model.StatusEnum;
import com.naver.hackday.service.CommentService;
import com.naver.hackday.service.ReactCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("react")
public class ReactController {

  @Autowired
  private CommentService commentService;

  @Autowired
  private ReactCheckService reactCheckService;

  @GetMapping("{postId}/{commentId}/{userId}/{react}")
  public ResponseEntity<DefaultResponse> integrationReact(@PathVariable int postId,
                                                          @PathVariable int commentId,
                                                          @PathVariable int userId,
                                                          @PathVariable String react) {
    DefaultResponse res = new DefaultResponse();

    // 해당 commentId의 댓글이 존재하는지 찾습니다.
    CommentDto comment = commentService.findById(commentId);

    reactCheckService.reactCheck(postId, comment.getId(), userId, react);

    /*
    뷰에서 해당 사용자의 공감 요청에 따른
    공감수 변화(+1, -1)을 보여주기 위해
    공감수를 응답 바디에 채워 넘깁니다.
     */
    res.setData(comment.getPCount());
    res.setMsg(commentId + "번 댓글에 대한 요청입니다.");
    res.setStatusEnum(StatusEnum.SUCCESS);
    return new ResponseEntity<>(res, HttpStatus.OK);

  }

}
