package com.github.sensitive.plugin.strategy;


import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public final class Message {

    final Method method;
    final Object payload;
    final Purpose purpose;
    final TypeKind typeKind;
    final List<TypeKind> ignoredTypeKind;
    final Optional<Annotation> annotation;

    public Message(Method method, Object payload, Purpose purpose, TypeKind typeKind, List<TypeKind> ignoredTypeKind, Optional<Annotation> annotation) {
        this.method = method;
        this.payload = payload;
        this.purpose = purpose;
        this.typeKind = typeKind;
        this.ignoredTypeKind = ignoredTypeKind;
        this.annotation = annotation;
    }

    public static Message of(Method method, Object payload, Purpose purpose, Optional<Annotation> annotation, List<TypeKind> ignoredTypeKind){
        TypeKind typeKind = TypeClassifier.classify(payload);
        return new Message(method,payload,purpose,typeKind,ignoredTypeKind,annotation);
    }

    public Method getMethod() {
        return method;
    }

    public Object getPayload() {
        return payload;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public List<TypeKind> getIgnoredTypeKind() {
        return ignoredTypeKind;
    }

    public Optional<Annotation> getAnnotation() {
        return annotation;
    }
}
