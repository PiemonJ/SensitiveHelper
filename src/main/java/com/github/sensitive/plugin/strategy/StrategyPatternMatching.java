package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 策略模式匹配类
 */
public class StrategyPatternMatching {

    static Strategy strategy;


    public static Object matching(Object data, Annotation annotation, Purpose purpose, TypeKind... skips){

        TypeKind typeKind = TypeClassifier.classify(data);

        if (Arrays.asList(skips).contains(typeKind))
            return data;

        return matching(data, typeKind, annotation, purpose);

    }


    public static Object matching(Object data, TypeKind typeKind,Annotation annotation, Purpose purpose){

        switch (typeKind){
            case LIST:
                // 递归
                data = strategy.action(data, annotation, purpose);
                break;
            case MAP:
                // 可能会出现这种情况,但是一般不会出现
                data = strategy.action(data, annotation, purpose);
                break;
            case STRING:
                // List列表中数据是基础数据类型
                data = strategy.action(data, annotation, purpose);
                break;
            case MODEL:
                // 假定是领域模型
                data = strategy.action(data, annotation, purpose);
                break;
            case PRIMITIVE:
            case UNKNOWN:
            default:
                break;
        }

        return data;
    }

}
