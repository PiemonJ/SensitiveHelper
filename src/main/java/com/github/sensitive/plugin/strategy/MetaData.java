package com.github.sensitive.plugin.strategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public final class MetaData {

    Method method;

    Optional<Annotation> annotation;

    public MetaData(Method method, Optional<Annotation> annotation) {
        this.method = method;
        this.annotation = annotation;
    }

    public static MetaData of(Method method){
        return new MetaData(method,Optional.empty());
    }

    public static MetaData of(Method method, Optional<Annotation> annotation){
        return new MetaData(method,annotation);
    }

}
