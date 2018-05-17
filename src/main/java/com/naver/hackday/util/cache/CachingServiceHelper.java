package com.naver.hackday.util.cache;

import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CachingServiceHelper<T, R> {

    @SuppressWarnings("unchecked")
    @Transactional
    public boolean isNeedCaching(Supplier<Object> invokeApi,
                                 Consumer<T> deleteApi,
                                 Function<T, Object> getDataApi,
                                 T deleteKey, T reactTimeKey) {
        //데이터가 캐싱되어 있지 않거나 사용자의 마지막 공감 시간과 데이터가 캐싱된 시간을 비교하여 데이터의 유효성을 검사
        if (invokeApi.get() == null) {
            deleteApi.accept(deleteKey);
            return true;
        }

        Map<String, Long> validCaching = (LinkedHashMap<String, Long>)invokeApi.get();
        long cachingTime = validCaching.get("cachingTime");
        long reactTime =  Long.parseLong(getDataApi.apply(reactTimeKey).toString());

        if (cachingTime - reactTime < 0) {
            return true;
        }

        return false;
    }

    public R useCaching(Function<T, R> invokeApi, T key) {
        return invokeApi.apply(key);
    }

}
