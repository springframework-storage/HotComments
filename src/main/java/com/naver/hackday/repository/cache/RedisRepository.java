package com.naver.hackday.repository.cache;

import java.util.List;

public interface RedisRepository extends CachingRespository {

    Long setListToListRight(String key, Object data);
    List<Object> getRangeFromList(String key, long start, long end);
    Long addMemberToSet(String key, Object data);
    Boolean isMember(String key, Object data);

}
