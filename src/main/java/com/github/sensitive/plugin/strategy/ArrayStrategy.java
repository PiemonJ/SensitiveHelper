package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;

import java.lang.reflect.Array;

public class ArrayStrategy extends AbstractStrategy {
    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) throws Throwable {

        Array array = (Array) data;
        return null;
    }
}
