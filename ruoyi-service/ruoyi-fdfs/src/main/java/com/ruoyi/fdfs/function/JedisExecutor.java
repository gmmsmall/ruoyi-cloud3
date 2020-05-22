package com.ruoyi.fdfs.function;


import com.ruoyi.fdfs.domain.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
