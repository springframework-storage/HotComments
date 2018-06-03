package com.naver.hackday.model.codingsquid;

import lombok.Getter;

@Getter
public enum BaseCode {
  SUCCESS(200, "응답 성공"),
  BAD_REQUEST(400, "잘못된 파라미터 요청"),
  SERVER_ERROR(500, "서버 에러");

  private final int code;
  private final String message;

  BaseCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
