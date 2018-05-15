package com.naver.hackday.util.cache;

import java.util.function.Function;
import java.util.function.Predicate;

public class CachingServiceHelper<T, R> {

    public boolean isNeedCaching(Predicate<String> invokeApi, String key) {
        return !invokeApi.test(key);
    }

    public R useCaching(Function<T, R> invokeApi, T key) {
        return invokeApi.apply(key);
    }

}
