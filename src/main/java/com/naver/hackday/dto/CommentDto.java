package org.naver.hackday.dto;

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

    @JsonProperty("comment_id")
    private int id;
    @JsonProperty("post_id")
    private int postId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("dislike_count")
    private int dislikeCount;
    @JsonProperty("total_react_count")
    private int totalReactCount;

}
