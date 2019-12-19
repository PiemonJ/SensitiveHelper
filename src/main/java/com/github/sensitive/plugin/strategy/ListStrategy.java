package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ListStrategy extends AbstractStrategy{

    @Override
    public Object action(Object data, Annotation annotation, Purpose purpose) {
        List collection = (List) data;

        for (int index = 0; index < collection.size(); index++) {

            Object element = collection.get(index);

            collection.set(index,StrategyPatternMatching.matching(element, annotation, purpose, TypeKind.PRIMITIVE, TypeKind.STRING, TypeKind.MAP));
        }

        return data;
    }
}
