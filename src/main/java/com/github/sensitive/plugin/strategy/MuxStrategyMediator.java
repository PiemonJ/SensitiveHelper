package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MuxStrategyMediator implements StrategyMediator {

    public ListStrategy listStrategy;

    public MapStrategy mapStrategy;

    public StringStrategy stringStrategy;

    public ModelStrategy modelStrategy;

    @Override
    public Object communicate(Message message) throws Throwable {

        if (message.getAnnotation().isPresent()) {
            Object payload = message.getPayload();
            TypeKind typeKind = TypeClassifier.classify(payload);
            if (!message.getIgnoredTypeKind().contains(typeKind)){
                return matching(payload, typeKind, MetaData.of(message.method,message.annotation), message.getPurpose());
            }
        }
        return message.getPayload();

    }

    public Object matching(Object data, TypeKind typeKind, MetaData metaData, Purpose purpose) throws Throwable {
        switch (typeKind){
            case LIST:
                // 递归
                data = listStrategy.action(data, metaData, purpose);
                break;
            case MAP:
                // 可能会出现这种情况,但是一般不会出现
                data = mapStrategy.action(data, metaData, purpose);
                break;
            case STRING:
                // List列表中数据是基础数据类型
                data = stringStrategy.action(data, metaData, purpose);
                break;
            case MODEL:
                // 假定是领域模型
                data = modelStrategy.action(data, metaData, purpose);
                break;
            case PRIMITIVE:
            case UNKNOWN:
            default:
                break;
        }
        return data;
    }
}
