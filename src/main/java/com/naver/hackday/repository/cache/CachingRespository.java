package com.naver.hackday.repository.cache;

public interface CachingRespository {

    void setData(String key, Object data);
    Object getData(String key);
    void setExpiredTimeMillisec(String key, Long expiredTime);
    Long getExpiredTimeMillisec(String key);

}
