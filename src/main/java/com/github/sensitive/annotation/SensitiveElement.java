package com.github.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 单元素的敏感属性
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SensitiveElement {

    String value();

}
