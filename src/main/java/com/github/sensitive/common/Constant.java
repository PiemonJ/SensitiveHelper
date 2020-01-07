package com.github.sensitive.common;

import com.github.sensitive.annotation.SensitiveAny;
import com.github.sensitive.annotation.SensitiveScalar;
import sun.reflect.annotation.AnnotationParser;

import java.util.HashMap;

public class Constant {

    public static SensitiveScalar SCALAR;

    public static SensitiveAny ANY;


    static {
        HashMap<String, Object> sensitiveScalarConfig = new HashMap<String, Object>();
        SCALAR = (SensitiveScalar) AnnotationParser.annotationForMap(SensitiveScalar.class, sensitiveScalarConfig);
        HashMap<String, Object> sensitiveAnyConfig = new HashMap<String, Object>();
        ANY = (SensitiveAny) AnnotationParser.annotationForMap(SensitiveAny.class, sensitiveAnyConfig);
    }
}
