package com.github.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 只能存在于@SensitiveMap中
 * 用于指示该条目对应的value是一个敏感项，
 * 敏感项可以是Model/String/List/Array
 *
 * String比较特殊，当指定了String，那么忽略版本号，直接加密处理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface SensitiveEntry {
}
