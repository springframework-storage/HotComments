package com.naver.hackday.model.codingsquid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseListRtn<T> {

  @JsonProperty("total_size")
  protected int totalSize;

  @JsonProperty("data_list")
  protected List<T> datas;

}
