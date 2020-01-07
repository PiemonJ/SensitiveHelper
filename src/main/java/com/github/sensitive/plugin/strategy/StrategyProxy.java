package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.*;
import com.github.sensitive.cache.Cache;
import com.github.sensitive.cache.CacheFactory;
import com.github.sensitive.enums.Purpose;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 静态代理
 */

public class StrategyProxy extends AbstractStrategy {

    public static final String DEFAULT_PARAM_NAME = "SINGLE";

    Cache<String,Object> cache = CacheFactory.<String,Object>instance(null);

    public boolean whetherParameterNonExisted(Method method) {
        return method.getParameterTypes().length == 0;
    }


    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) throws Throwable {

        if (whetherParameterNonExisted(metaData.method)) {
            return data;
        }

        if (data instanceof DefaultSqlSession.StrictMap){
            //单参数,且是List,Mybatis将其包装为DefaultSqlSession.StrictMap
            DefaultSqlSession.StrictMap casting = (DefaultSqlSession.StrictMap) data;
            hepler(casting, metaData, purpose);
        } else if (data instanceof MapperMethod.ParamMap){
            // 多参数,Mybatis将其包装为MapperMethod.ParamMap
            MapperMethod.ParamMap casting = (MapperMethod.ParamMap) data;
            hepler(casting, metaData, purpose);
        } else if (data instanceof Map){
            // 单参数,参数类型是Map,Mybatis并不做额外处理,使用原始类型
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, data);
            hepler(wrapper, metaData, purpose);
            data = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            // 单参数,Mybatis并未做额外的处理，一般情况是Model/Number/String
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, data);
            hepler(wrapper, metaData, purpose);
            data = wrapper.get(DEFAULT_PARAM_NAME);
        }
        return data;
    }


    public void hepler(Map<String, Object> mybatisMap, MetaData metaData, Purpose purpose) throws Throwable {

        List<Annotation> sensitiveAnnotations = Arrays.stream(metaData.method.getParameterAnnotations())
                .flatMap(
                        annotations -> {
                            return Arrays.stream(annotations)
                                    .filter(whetherSensitiveAnnotated)
                                    .findFirst()
                                    .map(Stream::of)
                                    .orElseGet(Stream::empty);
                        }
                ).collect(Collectors.toList());

        // 为了向上传播异常
        for (Annotation sensitiveAnnotation : sensitiveAnnotations) {

            Optional<String> optionName = obtainParamNameOf(sensitiveAnnotation);
            if (optionName.isPresent()){

                String nameOfParam = optionName.get();
                Object valueOfParam = mybatisMap.get(optionName.get());
                Message message = Message.of(metaData.method,valueOfParam, purpose, Optional.ofNullable(sensitiveAnnotation), Collections.emptyList());
                Object result = StrategyMediator.mediator.communicate(message);
                mybatisMap.put(nameOfParam,result);
            }

        }
//        Arrays.stream(metaData.method.getParameterAnnotations())
//                .flatMap(
//                        annotations -> {
//                            return Arrays.stream(annotations)
//                                    .filter(whetherSensitiveAnnotated)
//                                    .findFirst()
//                                    .map(Stream::of)
//                                    .orElseGet(Stream::empty);
//                        }
//                ).forEach(
//                        annotation -> {
//                            obtainParamNameOf(annotation).ifPresent(
//                                    paramName ->
//                                            Optional.ofNullable(mybatisMap.get(paramName)).ifPresent(
//                                                    data -> {
//                                                        try {
//                                                            mybatisMap.put(paramName, mediator.communicate(Message.of(data, purpose, Optional.ofNullable(annotation), Collections.emptyList())));
//                                                        } catch (Throwable throwable) {
//
//                                                        }
//                                                    }
//                                            )
//                            );
//                        }
//                );
    }

    public Optional<String> obtainParamNameOf(Annotation annotation) {

        if (annotation instanceof SensitiveMap){
            return Optional.ofNullable(((SensitiveMap) annotation).name());
        } else if (annotation instanceof SensitiveCollection) {
            return Optional.ofNullable(((SensitiveCollection) annotation).name());
        } else if (annotation instanceof SensitiveElement) {
            return Optional.ofNullable(((SensitiveElement) annotation).name());
        } else if (annotation instanceof SensitiveScalar) {
            return Optional.ofNullable(((SensitiveScalar) annotation).name());
        } else {
            return Optional.empty();
        }

    }


    public Predicate<Annotation> whetherSensitiveAnnotated = annotation ->
            annotation instanceof SensitiveCollection
                    || annotation instanceof SensitiveMap
                    || annotation instanceof SensitiveElement
                    || annotation instanceof SensitiveArray
                    || annotation instanceof SensitiveEntry
                    || annotation instanceof SensitiveScalar;


}
