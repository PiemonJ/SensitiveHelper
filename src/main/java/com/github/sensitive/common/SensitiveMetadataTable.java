package com.github.sensitive.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SensitiveMetadataTable {


    final Method method;

    final boolean sensitive;

    final String methodName;

    final Class<?> returnType;

    final List<SensitiveMetadataEntry> zipping;

    public static SensitiveMetadataTable of(Method method) {
        List<SensitiveMetadataEntry> zipping = new ArrayList<>();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < paramAnnotations.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Annotation[] annotations = paramAnnotations[i];
            zipping.add(SensitiveMetadataEntry.of(method, i, parameterType, annotations));
        }
        boolean sensitive = zipping.stream().anyMatch(zip -> zip.getSensitiveAnnotation().isPresent());
        final Class<?> returnType = method.getReturnType();
        return new SensitiveMetadataTable(method, sensitive, method.getName(), returnType, zipping);
    }

    public List<SensitiveMetadataEntry> sensitive() {
        return zipping.stream().filter(zip -> zip.getSensitiveAnnotation().isPresent()).collect(Collectors.toList());
    }

}
