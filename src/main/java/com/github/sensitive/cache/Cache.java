package com.github.sensitive.cache;

import java.util.Optional;

public interface Cache<K,V> {

    Optional<V> obtain(K key);

    void setting(K key,V value);

}
