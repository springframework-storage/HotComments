package com.naver.hackday.repository.cache;

import com.naver.hackday.dto.CommentDto;

import java.util.List;

public interface RedisRepository extends CachingRespository {

  Long setListToListRight(String key, Object data, Long expiredTime);

  List<Object> getRangeFromList(String key, long start, long end);

  Object getDataOfHashKey(String key, String HashKey);

  void addDataOfHashKey(String key, String HashKey, Object data, Long expiredTime);

  boolean hasKey(String key, String hashKey);

  boolean isMember(String key, int member);

  List<CommentDto> getRangeFromListForCommentDto(String key, long start, long end);

}
