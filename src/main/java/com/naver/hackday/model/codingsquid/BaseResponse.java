package com.naver.hackday.model.codingsquid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BaseResponse<T> {

  public final static String RETURN_CODE = "return_code";
  public final static String RETURN_MESSAGE = "return_message";
  public final static String RESULT = "result";

  @JsonProperty(RETURN_CODE)
  private int returnCode;
  @JsonProperty(RETURN_MESSAGE)
  private String returnMessage;
  @JsonProperty(RESULT)
  private T result;

  public BaseResponse() {
  }

  public BaseResponse(int returnCode, String returnMessage) {
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
  }

}
