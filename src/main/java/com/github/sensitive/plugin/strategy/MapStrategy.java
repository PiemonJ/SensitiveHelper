package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.*;
import com.github.sensitive.enums.Purpose;
import java.util.Map;
import java.util.Optional;

public class MapStrategy extends AbstractStrategy {


    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) throws Throwable {
        Map<String, Object> map = (Map<String, Object>) data;


        SensitiveMap annotation = (SensitiveMap) metaData.annotation.get();

        for (SensitiveEntry sensitiveEntry : annotation.entries()) {
            String key = sensitiveEntry.key();
            Object value = map.get(key);
            Object result = StrategyMediator.mediator.communicate(Message.of(metaData.method, value, purpose, Optional.of(annotation), IGNORED));
            map.put(key,result);
        }


//        if (annotation instanceof SensitiveCollection)
//            whenSensitiveCollection((SensitiveCollection)annotation, mybatisMap, purpose);
//        else if (annotation instanceof SensitiveElement)
//            whenSensitiveElement((SensitiveElement)annotation, mybatisMap, purpose);
        return data;
    }


//    public void whenSensitiveCollection(SensitiveCollection annotation, Map<String, Object> mybatisMap, Purpose purpose) {
//
//        Arrays.stream(annotation.elements())
//                .forEach(
//                        element ->
//                            Optional.ofNullable((Map<String, Object>) mybatisMap.get(annotation.value()))
//                                    .ifPresent(
//                                            value -> {
//                                                whenSensitiveElement(element, value, purpose);
//                                            }
//                                    )
//                );
//    }
//
//    public void whenSensitiveElement(SensitiveElement annotation, Map<String, Object> mybatisMap, Purpose purpose) {
//
//        Stream.of(annotation)
//                .forEach(
//                        element -> {
//                            String name = element.value();
//                            Optional.ofNullable(mybatisMap.get(name))
//                                    .ifPresent(
//                                        value -> mybatisMap.put(
//                                                name,
//                                                StrategyPatternMatching.matching(
//                                                        value,
//                                                        element,
//                                                        purpose,
//                                                        TypeKind.MAP
//                                                ))
//                            );
//                        }
//                );
//    }

}
