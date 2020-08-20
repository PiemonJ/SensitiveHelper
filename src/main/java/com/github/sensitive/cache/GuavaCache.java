package com.github.sensitive.cache;

import com.google.common.cache.CacheBuilder;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class GuavaCache<K, V> implements Cache<K, V> {

    private final com.google.common.cache.Cache<K, V> CACHE;

    public GuavaCache(Properties properties) {

        CACHE = CacheBuilder.newBuilder()
                .maximumSize(1_000)
                .build();
    }


    @Override
    public Optional<V> cas(K key, Callable<V> callback) throws ExecutionException {

        return Optional.ofNullable(CACHE.get(key,callback));

    }


}
