package com.github.sensitive.utils;

import com.github.sensitive.enums.TypeKind;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeClassifier {

    /**
     * 对某一个Class进行分类
     * @param clazz
     * @return
     */
    public static TypeKind classify(Class<?> clazz){
        if (clazz == null)
            return TypeKind.UNKNOWN;
        else if (clazz == Map.class || clazz == HashMap.class)
            return TypeKind.MAP;
        else if (clazz == List.class || clazz == ArrayList.class)
            return TypeKind.LIST;
        else if (clazz == String.class)
            return TypeKind.PRIMITIVE;
        else if (clazz == Enum.class
                || clazz == boolean.class  || clazz == Boolean.class
                || clazz == byte.class     || clazz == Byte.class
                || clazz == short.class    || clazz == Short.class
                || clazz == int.class      || clazz == Integer.class
                || clazz == long.class     || clazz == Long.class
                || clazz == float.class    || clazz == Float.class
                || clazz == double.class   || clazz == Double.class
                || clazz == BigDecimal.class)
            return TypeKind.PRIMITIVE;
        else
            return TypeKind.MODEL;

    }


    public static TypeKind classify(Object obj){

        if (obj == null)
            return TypeKind.UNKNOWN;
        else if (obj instanceof Map)
            return TypeKind.MAP;
        else if (obj instanceof List)
            return TypeKind.LIST;
        else if (obj instanceof String)
            return TypeKind.PRIMITIVE;
        else if (obj instanceof Enum
                || obj instanceof Boolean
                || obj instanceof Byte
                || obj instanceof Short
                || obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Float
                || obj instanceof Double
                || obj instanceof BigDecimal)
            return TypeKind.PRIMITIVE;
        else
            return TypeKind.MODEL;
    }
}
