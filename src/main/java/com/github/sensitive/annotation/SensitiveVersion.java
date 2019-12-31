package com.github.sensitive.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveVersion {
}
