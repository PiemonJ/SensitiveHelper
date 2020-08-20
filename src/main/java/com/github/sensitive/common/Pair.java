package com.github.sensitive.common;


public class Pair<K,V> {

    K key;

    V value;


    public static <A,B> Pair<A,B> of(A key,B value){
        return new Pair<>(key,value);
    }

    private Pair() {
    }

    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
