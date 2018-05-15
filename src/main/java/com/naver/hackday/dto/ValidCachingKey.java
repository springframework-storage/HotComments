package com.naver.hackday.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidCachingKey {

    @JsonProperty("valid_key")
    private String validKey;

}
