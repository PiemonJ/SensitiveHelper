package com.github.sensitive.common;

import com.github.sensitive.annotation.*;
import com.github.sensitive.annotation.SensitiveMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.annotations.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
@Getter
@AllArgsConstructor
public class SensitiveMetadataEntry {

    final Method method;

    final int index;

    final Class<?> parameterType;

    final Annotation[] annotations;

    final Optional<Annotation> paramAnnotation;

    final Optional<Annotation> sensitiveAnnotation;


    public static SensitiveMetadataEntry of(Method method, int index, Class<?> parameterType, Annotation[] annotations) {
        Optional<Annotation> paramAnnotation = Arrays.stream(annotations).filter(annotation -> annotation instanceof Param).findFirst();
        Optional<Annotation> sensitiveAnnotation = Arrays.stream(annotations).filter(annotation -> {
            return annotation instanceof SensitiveArray
                    ||
                    annotation instanceof SensitiveScalar
                    ||
                    annotation instanceof SensitiveValue
                    ||
                    annotation instanceof SensitiveCollection
                    ||
                    annotation instanceof SensitiveElement
                    ||
                    annotation instanceof SensitiveMap;
        }).findFirst();
        return new SensitiveMetadataEntry(method,index,parameterType, annotations,paramAnnotation,sensitiveAnnotation);
    }

}
