package com.github.sensitive.plugin.strategy;


import com.github.sensitive.common.Constant;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public final class Message {

    Method method;
    Object payload;
    Purpose purpose;
    Optional<Annotation> annotation;
    List<TypeKind> ignoredTypeKind;

    public Message(Method method, Object payload, Purpose purpose, Optional<Annotation> annotation, List<TypeKind> ignoredTypeKind) {
        this.method = method;
        this.payload = payload;
        this.purpose = purpose;
        this.annotation = annotation;
        this.ignoredTypeKind = ignoredTypeKind;
    }

    public static Message of(Method method,Object payload, Purpose purpose, Optional<Annotation> annotation, List<TypeKind> ignoredTypeKind){
        return new Message(method,payload,purpose,annotation,ignoredTypeKind);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Optional<Annotation> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Optional<Annotation> annotation) {
        this.annotation = annotation;
    }

    public List<TypeKind> getIgnoredTypeKind() {
        return ignoredTypeKind;
    }

    public void setIgnoredTypeKind(List<TypeKind> ignoredTypeKind) {
        this.ignoredTypeKind = ignoredTypeKind;
    }
}
