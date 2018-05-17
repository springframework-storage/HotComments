package com.naver.hackday.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValidCachingKey {

    private String key;
    private long cachingTime;

}
