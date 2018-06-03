package com.naver.hackday.model.codingsquid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorModel {

  @JsonProperty("return_code")
  private int errorCode;
  @JsonProperty("return_message")
  private String errorMessage;

}
