package com.github.sensitive.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SensitiveMap {

    String name();

    SensitiveEntry[] entries();

    SensitiveElement[] elements();

    SensitiveCollection[] collections();
}
