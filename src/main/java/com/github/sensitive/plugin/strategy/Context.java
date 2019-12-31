package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.SensitiveCollection;
import com.github.sensitive.annotation.SensitiveElement;
import com.github.sensitive.annotation.SensitiveMap;
import com.github.sensitive.annotation.SensitiveScalar;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import com.github.sensitive.utils.TypeClassifier;
import org.apache.ibatis.annotations.Param;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.sensitive.plugin.strategy.StrategyPatternMatching.matching;

public class Context {

    final Method method;

    public boolean hasParamAnnotation = false;

    public static final String DEFAULT_PARAM_NAME = "SINGLE";

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
        Context context = new Context(method);

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        loop:
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    context.hasParamAnnotation = true;
                    break loop;
                }
            }
        }

        return context;
    }

    public Object ask(Object data, Purpose purpose) {

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

    private Object onDecrypt(Object output) {
        // Only！！处理查询端的返回值，不处理命令端的返回值(因为命令端只返回影响条数)
        if (output instanceof ArrayList) {
            if (Objects.isNull(output))
                return output;
            Class<?> returnType = method.getReturnType();
            TypeKind typeKind = TypeClassifier.classify(returnType);
            switch (typeKind) {
                case LIST:
                    // 1.方法签名中返回值是List
                    output = matching(output, typeKind, null, Purpose.DECRYPT);
                    break;
                case MAP:
                case MODEL:
                    // 2.方法签名中返回值是Map
                    // 3.方法的返回值类型是领域模型
                    ((ArrayList) output).set(
                            0,
                            matching(((ArrayList) output).get(0), typeKind, null, Purpose.DECRYPT)
                    );
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

    private Object onErasure(Object input) {

        if (whetherParameterNonExisted.test(method)) {
            return input;
        }
        if (!hasParamAnnotation && method.getParameterTypes().length == 1) {
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, input);
            doing(wrapper, Purpose.ERASURE);
            input = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            doing((Map<String, Object>) input, Purpose.ERASURE);
        }

        return input;
    }

    /**
     * 参数加密
     * 1.入参只有一个Map,eg：public void foo(Map map)
     * 2.入参有Map，并且有@param,eg：public void foo(@Param("xxx") Map map)
     * 3.入参有多个，无论参数类型是什么，eg:public void foo(String name,String age)
     * PS:原处替换~
     */
    private Object onEncrypt(Object input) {
        // 1.0 处理无参数的情况

        if (whetherParameterNonExisted.test(method)) {
            return input;
        }
        if (!hasParamAnnotation && method.getParameterTypes().length == 1) {
            HashMap<String, Object> wrapper = new HashMap<>();
            wrapper.put(DEFAULT_PARAM_NAME, input);
            doing(wrapper, Purpose.ENCRYPT);
            input = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            doing((Map<String, Object>) input, Purpose.ENCRYPT);
        }
        return input;
    }




    public Optional<String> obtainParamName(Annotation annotation){
        if (annotation instanceof SensitiveCollection) {
            return Optional.ofNullable(((SensitiveCollection) annotation).name());
        } else if (annotation instanceof SensitiveElement) {
            return Optional.ofNullable(((SensitiveElement) annotation).name());
        } else if (annotation instanceof SensitiveMap) {
            return Optional.ofNullable(((SensitiveElement) annotation).name());
        } else {
            return Optional.empty();
        }

    }

    public Predicate<Method> whetherParameterNonExisted = method -> method.getParameterTypes().length == 0;

    public Predicate<Annotation> whetherSensitiveAnnotated = annotation ->
            annotation instanceof SensitiveCollection
            || annotation instanceof SensitiveElement
            || annotation instanceof SensitiveMap;

    private void doing(Map<String, Object> mybatisMap, Purpose purpose) {

        Arrays.stream(method.getParameterAnnotations())
                .flatMap(
                        annotations -> {
                            return Arrays.stream(annotations)
                                    .filter(whetherSensitiveAnnotated)
                                    .findFirst()
                                    .map(Stream::of)
                                    .orElseGet(Stream::empty);
                        }
                ).forEach(
                        annotation -> {
                            obtainParamName(annotation).ifPresent(
                                    paramName ->
                                        Optional.ofNullable(mybatisMap.get(paramName)).ifPresent(
                                                data -> {
                                                    Object result = matching(data, annotation, purpose, TypeKind.MAP);
                                                    mybatisMap.put(paramName,result);
                                                }
                                        )
                            );
                        }
        );
    }
}