package com.naver.hackday.model;

import lombok.Data;

@Data
public class Response {

  private Object data;
  private String msg;
  private Status status;

  public Response() {
    this.data = null;
    this.msg = null;
    this.status = Status.FAIL;
  }

}
