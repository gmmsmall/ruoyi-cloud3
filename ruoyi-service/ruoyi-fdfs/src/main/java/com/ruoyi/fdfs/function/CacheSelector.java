package com.ruoyi.fdfs.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
