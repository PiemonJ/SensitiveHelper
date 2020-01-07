package com.github.sensitive.cache;

import java.util.Properties;

public class CacheFactory {

    public static <K,V> Cache<K,V> instance(Properties properties){
        return new GuavaCache<>(properties);
    }
}
