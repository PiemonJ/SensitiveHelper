package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.SensitiveCollection;
import com.github.sensitive.annotation.SensitiveElement;
import com.github.sensitive.annotation.SensitiveScalar;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class MapStrategy extends AbstractStrategy {


    @Override
    public Object action(Object data, Annotation annotation, Purpose purpose) {
        Map<String, Object> mybatisMap = (Map<String, Object>) data;

        if (annotation instanceof SensitiveCollection)
            whenSensitiveCollection(annotation, mybatisMap, purpose);
        else if (annotation instanceof SensitiveElement)
            whenSensitiveElement(annotation, mybatisMap, purpose);
        return data;
    }


    public void whenSensitiveCollection(Annotation annotation, Map<String, Object> mybatisMap, Purpose purpose) {

        Arrays.asList(((SensitiveCollection) annotation).elements())
                .stream()
                .forEach(
                        element -> {
                            String name = ((SensitiveCollection) annotation).value();
                            Optional.ofNullable((Map<String, Object>) mybatisMap.get(name))
                                    .ifPresent(
                                            value -> {
                                                whenSensitiveElement(element, value, purpose);
                                            }
                                    );


                        }
                );
    }

    public void whenSensitiveElement(Annotation annotation, Map<String, Object> mybatisMap, Purpose purpose) {

        Stream.of((SensitiveElement) annotation)
                .forEach(
                        element -> {
                            String name = element.value();
                            Optional.ofNullable(mybatisMap.get(name)).ifPresent(
                                    value -> mybatisMap.put(
                                            name,
                                            StrategyPatternMatching.matching(
                                                    value,
                                                    element,
                                                    purpose,
                                                    TypeKind.MAP
                                            ))
                            );
                        }
                );
    }

//
//    public void whenSensitiveScalar(Annotation annotation, Map<String, Object> mybatisMap, Purpose purpose) {
//        Stream.of((SensitiveScalar) annotation)
//                .forEach(
//                        element -> {
//                            String name = element.value();
//                            Optional.ofNullable(mybatisMap.get(name)).ifPresent(
//                                    value -> mybatisMap.put(
//                                            name,
//                                            StrategyPatternMatching.matching(
//                                                    value,
//                                                    element,
//                                                    purpose
//                                            ))
//                            );
//                        }
//                );
//    }
}
