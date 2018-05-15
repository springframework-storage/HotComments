package com.naver.hackday.util.cache;

import java.util.function.*;

public class CachingServiceHelper<T, R> {

    public boolean isNeedCaching(Supplier<Object> invokeApi) {
        return invokeApi.get() == null;
    }

    public R useCaching(Function<T, R> invokeApi, T key) {
        return invokeApi.apply(key);
    }

}
