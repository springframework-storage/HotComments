package com.naver.hackday.model;

public enum ReactParam {
  Pst(1), Ngt(2);

  private int value;

  ReactParam(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

}
