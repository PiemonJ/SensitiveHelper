package com.github.sensitive.common;


import java.util.HashMap;

public class SensitiveMap<V> extends HashMap<String,V> {

    private static <T> SensitiveMap<T> empty(){
        return new SensitiveMap<T>();
    }

    public static <T> SensitiveMap<T> singleton(String key,T value){
        SensitiveMap<T> container = SensitiveMap.<T>empty();
        container.put(key,value);
        return container;
    }



}
