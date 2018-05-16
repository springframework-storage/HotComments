package com.naver.hackday.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CommentDto {

    private int id;
    private int postId;
    private int userId;
    private String content;
    private int pCount;
    private int nCount;
    private int totalCount;

}
