package com.naver.hackday.repository.cache;

public interface CachingRespository {

    void setData(String key, Object data, Long expiredTime);
    Object getData(String key);

}
