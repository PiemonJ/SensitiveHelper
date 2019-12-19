package com.github.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 集合类型的特殊敏感参数
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SensitiveCollection {

    String value();

    SensitiveElement[] elements();

    SensitiveScalar[] scalars();
}
