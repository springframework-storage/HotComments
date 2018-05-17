package com.naver.hackday.controller;

import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.model.DefaultResponse;
import com.naver.hackday.model.ReactParam;
import com.naver.hackday.model.Status;
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

  /**
   * 사용자의 공감 요청을 받습니다.
   * @param postId
   * @param commentId
   * @param userId
   * @param reactParam : Enum(Pst,Ngt)로 제약이 있도록 합니다.
   * @return
   */
  @GetMapping("{postId}/{commentId}/{userId}/{reactParam}")
  public ResponseEntity<DefaultResponse> integrationReact(@PathVariable int postId,
                                                          @PathVariable int commentId,
                                                          @PathVariable int userId,
                                                          @PathVariable ReactParam reactParam) {
    DefaultResponse res = new DefaultResponse();

    // 해당 commentId의 댓글이 존재하는지 찾습니다.
    CommentDto comment = commentService.findById(commentId);

    /*
    CheckService로 파라미터를 넘길 때
    reactParam의 Value를 꺼내 int로 변환합니다.
     */
    reactCheckService.reactCheck(postId, comment.getId(), userId, reactParam.getValue());

    /*
    Comment 자체를 넘김으로서
    프론트에서 Comment의 각 요소들을 활용할 수 있도록 제공합니다.
     */
    res.setData(comment);
    res.setMsg(commentId + "번 댓글에 대한 요청입니다.");
    res.setStatus(Status.SUCCESS);
    return new ResponseEntity<>(res, HttpStatus.OK);

  }

}
