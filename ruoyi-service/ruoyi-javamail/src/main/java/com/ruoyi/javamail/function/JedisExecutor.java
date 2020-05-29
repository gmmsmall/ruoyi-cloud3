package com.ruoyi.javamail.function;

import com.ruoyi.javamail.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
