package com.github.sensitive.plugin.strategy;

import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public interface Strategy {


    public Object action(Object data, Annotation annotation, Purpose purpose);

//    /**
//     * 加密,原处修改
//     * @param param
//     * @param annotation
//     */
//    public Object encrypt(Object param, Annotation annotation);
//
//    /**
//     * 擦除
//     * @param param
//     * @param annotation
//     */
//    public Object erasure(Object param, Annotation annotation);
//
//    /**
//     * 解密,原处修改
//     * @param result
//     * @param annotation
//     */
//    public Object decrypt(Object result, Annotation annotation);
}
