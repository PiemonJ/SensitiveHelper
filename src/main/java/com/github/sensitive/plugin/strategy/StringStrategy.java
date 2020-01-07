package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public class StringStrategy extends AbstractStrategy {

    /**
     * 字符串策略是一个特殊的策略
     * 该策略不能由策略之外的对象触发
     * @param data
     * @param metaData
     * @param purpose
     * @return
     */

    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) {
        if (data == null)
            return null;

        switch (purpose) {
            case ENCRYPT:
                break;
            case ERASURE:
                break;
            case DECRYPT:
                break;
        }
        return data;
    }
}
