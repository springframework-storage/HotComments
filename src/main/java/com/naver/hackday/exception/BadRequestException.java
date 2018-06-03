package com.naver.hackday.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {

  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST.value(), message);
  }

}
