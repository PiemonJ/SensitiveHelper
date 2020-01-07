package com.github.sensitive.cache;

import com.google.common.cache.CacheBuilder;

import java.util.Optional;
import java.util.Properties;

public class GuavaCache<K, V> implements Cache<K, V> {

    private final com.google.common.cache.Cache<K, V> CACHE;

    public GuavaCache(Properties properties) {

        CACHE = CacheBuilder.newBuilder()
                .maximumSize(1_000)
                .build();
    }


    @Override
    public Optional<V> obtain(K key) {

        return Optional.ofNullable(CACHE.getIfPresent(key));
    }

    @Override
    public void setting(K key, V value) {
        CACHE.put(key,value);
    }
}
