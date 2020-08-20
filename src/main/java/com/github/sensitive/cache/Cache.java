package com.github.sensitive.cache;

import java.util.Optional;
import java.util.concurrent.Callable;

public interface Cache<K, V> {

    Optional<V> cas(K key, Callable<V> callback) throws Exception;


}
