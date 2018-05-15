package com.naver.hackday.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CommentDto {

    @JsonProperty("commentId")
    private int id;
    @JsonProperty("postId")
    private int postId;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("nCount")
    private int nCount;
    @JsonProperty("pCount")
    private int pCount;
    @JsonProperty("totalCount")
    private int totalCount;

}
