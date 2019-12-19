package com.github.sensitive.plugin.strategy;


import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AbstractStrategy implements Strategy {


    // 全局配置的版本名称
    public static final String GLOBAL_VERSION_NAME = "sensitiveVersion";


//    @Override
//    public Object encrypt(Object param, Method method) {
//        return template(param, method, Purpose.ENCRYPT);
//    }
//
//    @Override
//    public Object erasure(Object param, Method method) {
//        return template(param, method, Purpose.ERASURE);
//    }
//
//    @Override
//    public Object decrypt(Object result, Method method) {
//        return template(result, method, Purpose.DECRYPT);
//    }

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
