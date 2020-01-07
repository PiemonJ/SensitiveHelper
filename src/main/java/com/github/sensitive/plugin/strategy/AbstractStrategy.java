package com.github.sensitive.plugin.strategy;


import com.github.sensitive.annotation.SensitiveScalar;
import com.github.sensitive.enums.TypeKind;
import sun.reflect.annotation.AnnotationParser;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractStrategy implements Strategy {

    public static final List<TypeKind> IGNORED = Arrays.asList(TypeKind.MAP);


    /**
     * 乐观实现,补货最新数据载体中的版本号，对于领域模型 ，自然就是version属性
     * 对于Map,则可能是其中一个属性，一般也是version
     * @param data
     * @return
     */
    public Integer obtainBoundedVersion(Object data){
        return 0;
    };

}
