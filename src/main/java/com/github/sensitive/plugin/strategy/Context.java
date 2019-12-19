package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.SensitiveCollection;
import com.github.sensitive.annotation.SensitiveElement;
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
                    output = StrategyPatternMatching.matching(output, typeKind, null, Purpose.DECRYPT);
                    break;
                case MAP:
                case MODEL:
                    // 2.方法签名中返回值是Map
                    // 3.方法的返回值类型是领域模型
                    ((ArrayList) output).set(
                            0,
                            StrategyPatternMatching.matching(((ArrayList) output).get(0), typeKind, null, Purpose.DECRYPT)
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

        if (whetherNonParameter.test(method)) {
            return input;
        }
        if (!hasParamAnnotation && method.getParameterTypes().length == 1) {
            Map<String, Object> wrapper = Collections.singletonMap(DEFAULT_PARAM_NAME, input);
            whenSensitiveAnnotation(wrapper, Purpose.ERASURE);
            input = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            whenSensitiveAnnotation((Map<String, Object>) input, Purpose.ERASURE);
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

        if (whetherNonParameter.test(method)) {
            return input;
        }
        if (!hasParamAnnotation && method.getParameterTypes().length == 1) {
            Map<String, Object> wrapper = Collections.singletonMap(DEFAULT_PARAM_NAME, input);
            whenSensitiveAnnotation(wrapper, Purpose.ENCRYPT);
            input = wrapper.get(DEFAULT_PARAM_NAME);
        } else {
            whenSensitiveAnnotation((Map<String, Object>) input, Purpose.ENCRYPT);
        }


//        else if (hasParamAnnotation && parameterCount == 1) {
//            // Map 但是只有一个入参的Map
//            Map<String, Object> mybatisMap = (Map<String, Object>) input;
//            Annotation[] annotations = method.getParameterAnnotations()[0];
//            Arrays.stream(annotations)
//                    .filter(annotation ->
//                            annotation instanceof SensitiveCollection
//                                    || annotation instanceof SensitiveElement
//                                    || annotation instanceof SensitiveScalar)
//                    .findFirst()
//                    .ifPresent(
//                            annotation -> {
//                                if (annotation instanceof SensitiveCollection) {
//                                    whenSensitiveCollection(annotation,mybatisMap);
//                                } else if (annotation instanceof SensitiveElement) {
//                                    whenSensitiveElement(annotation,mybatisMap);
//                                }
//                            }
//                    );
//
//        } else {
//            whenSensitiveAnnotation((Map<String, Object>) input);
//        }

        return input;
    }


    public Predicate<Method> whetherNonParameter = method -> method.getParameterTypes().length == 0;

    public Predicate<Annotation> whetherSensitiveAnnotation = annotation ->
            annotation instanceof SensitiveCollection || annotation instanceof SensitiveElement;


    public void whenSensitiveAnnotation(Map<String, Object> mybatisMap, Purpose purpose) {

        Arrays.stream(method.getParameterAnnotations())
                .map(Arrays::stream)
                .forEach(
                        workerUnit -> {
                            workerUnit.filter(whetherSensitiveAnnotation)
                                    .findFirst()
                                    .ifPresent(
                                            annotation -> {
                                                Optional<String> optionName = Optional.empty();
                                                if (annotation instanceof SensitiveCollection) {
                                                    optionName = Optional.of(((SensitiveCollection) annotation).value());
                                                } else if (annotation instanceof SensitiveElement) {
                                                    optionName = Optional.of(((SensitiveElement) annotation).value());
                                                }
                                                optionName.ifPresent(
                                                        name -> Optional.ofNullable(mybatisMap.get(name)).ifPresent(
                                                                value -> mybatisMap.put(
                                                                        name,
                                                                        StrategyPatternMatching.matching(
                                                                                value,
                                                                                annotation,
                                                                                purpose,
                                                                                TypeKind.MAP
                                                                        ))
                                                        )
                                                );

                                            }
                                    );
                        }

                );
    }

}