package com.naver.hackday.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class BaseException extends RuntimeException {

  private int statusCode;

  protected BaseException(int statusCode) {
    this(statusCode, null);
  }

  BaseException(int statusCode, String message) {
    this(statusCode, message, null);
  }

  BaseException(int statusCode, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }

}
