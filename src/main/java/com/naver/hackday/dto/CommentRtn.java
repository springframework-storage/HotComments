package com.naver.hackday.dto;

import com.naver.hackday.model.codingsquid.ReactStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRtn {

  private int id;
  private int postId;
  private int userId;
  private String content;
  private int pCount;
  private int nCount;
  private int totalCount;
  private ReactStatus reactStatus;

}
