package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.*;
import com.github.sensitive.common.Constant;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;
import com.google.common.collect.Sets;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.defaults.DefaultSqlSession;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Context {

    public static final String DEFAULT_PARAM_NAME = "default";

    final Method method;

    public Context(Method method) {
        this.method = method;
    }

    /**
     * Helper Method
     *
     * @param method
     * @return
     */
    public static Context of(Method method) {
        return new Context(method);
    }

    public Object ask(Object data, Purpose purpose) throws Throwable {

        switch (purpose) {
            case ENCRYPT:
                data = onEncrypt(data);
                break;
            case ERASURE:
                data = onErasure(data);
                break;
            case DECRYPT:
                data = onDecrypt(data);
                break;
        }
        return data;
    }

    private Object onDecrypt(Object output) throws Throwable {
        // Only！！处理查询端的返回值，不处理命令端的返回值(因为命令端只返回影响条数)
        if (output instanceof ArrayList) {
            Class<?> returnType = method.getReturnType();
            TypeKind typeKind = TypeClassifier.classify(returnType);
            Message message;

            switch (typeKind) {
                case LIST:
                    // 1.方法签名中返回值是List
                    message = Message.of(method,output,Purpose.DECRYPT,Optional.of(Constant.ANY), Collections.emptyList());
                    output = StrategyMediator.mediator.communicate(message);
                    break;
                case MAP:
                case MODEL:
                    // 2.方法签名中返回值是Map
                    // 3.方法的返回值类型是领域模型
                    message = Message.of(method,((ArrayList) output).get(0),Purpose.DECRYPT,Optional.of(Constant.ANY),Collections.emptyList());
                    Object result = StrategyMediator.mediator.communicate(message);
                    ((ArrayList) output).set(0,result);
                    break;
                case STRING:
                case PRIMITIVE:
                case UNKNOWN:
                default:
                    // no-op
                    break;
            }

        }
        return output;

    }

    private Object onErasure(Object input) throws Throwable {
        return helper(input,Purpose.ENCRYPT);
    }


    private Object onEncrypt(Object input) throws Throwable {
        return helper(input,Purpose.ERASURE);
    }


    public Object helper(Object data, Purpose purpose) throws Throwable {

        if (method.getParameterTypes().length == 0) {
            return data;
        }

        if (data instanceof DefaultSqlSession.StrictMap){
            //单参数,且是List,Mybatis将其包装为DefaultSqlSession.StrictMap
            DefaultSqlSession.StrictMap casting = (DefaultSqlSession.StrictMap) data;
            skeleton(casting, purpose);
        } else if (data instanceof MapperMethod.ParamMap){
            // 多参数,Mybatis将其包装为MapperMethod.ParamMap
            MapperMethod.ParamMap casting = (MapperMethod.ParamMap) data;
            skeleton(casting, purpose);
        } else if (data instanceof Map){
            // 单参数,参数类型是Map,Mybatis并不做额外处理,使用原始类型
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, data);
            skeleton(wrapper, purpose);
            data = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            // 单参数,Mybatis并未做额外的处理，一般情况是Model/Number/String
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, data);
            skeleton(wrapper, purpose);
            data = wrapper.get(DEFAULT_PARAM_NAME);
        }
        return data;
    }


    public void skeleton(Map<String, Object> mybatisMap, Purpose purpose) throws Throwable {

        List<Annotation> sensitiveAnnotations = Arrays.stream(method.getParameterAnnotations())
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

            Optional<Set<String>> optionNames = obtainParamNameOf(sensitiveAnnotation);
            if (optionNames.isPresent()){
                for (String nameOfParam : optionNames.get()) {
                    Optional<Object> optionValueOfParam = Optional.ofNullable(mybatisMap.get(nameOfParam));
                    if (optionValueOfParam.isPresent()){
                        Object valueOfParam = optionValueOfParam.get();
                        Message message = Message.of(method,valueOfParam, purpose, Optional.ofNullable(sensitiveAnnotation), Collections.emptyList());
                        Object result = StrategyMediator.mediator.communicate(message);
                        mybatisMap.put(nameOfParam,result);
                    }
                }
            }

        }
    }

    public Optional<Set<String>> obtainParamNameOf(Annotation annotation) {

        if (annotation instanceof SensitiveMap){
            return Optional.of(Sets.newHashSet(((SensitiveMap) annotation).name()));
        } else if (annotation instanceof SensitiveCollection) {
            return Optional.of(Sets.newHashSet(((SensitiveCollection) annotation).name()));
        } else if (annotation instanceof SensitiveElement) {
            return Optional.of(Sets.newHashSet(((SensitiveElement) annotation).name()));
        } else if (annotation instanceof SensitiveScalar) {
            return Optional.of(Sets.newHashSet(((SensitiveScalar) annotation).name(),((SensitiveScalar) annotation).alias()));
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