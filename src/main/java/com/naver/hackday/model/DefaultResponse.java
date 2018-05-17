package com.naver.hackday.model;

import lombok.Data;

@Data
public class DefaultResponse {

  private Object data;
  private String msg;
  private Status status;

  public DefaultResponse() {
    this.data = null;
    this.msg = null;
    this.status = Status.FAIL;
  }

}
