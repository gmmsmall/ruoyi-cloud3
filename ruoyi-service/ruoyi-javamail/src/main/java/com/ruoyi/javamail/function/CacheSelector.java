package com.ruoyi.javamail.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
