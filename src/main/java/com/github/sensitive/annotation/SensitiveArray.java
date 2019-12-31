package com.github.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 用于标注数组类型的参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SensitiveArray {
}
