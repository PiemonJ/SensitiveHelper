package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.Sensitive;
import com.github.sensitive.annotation.SensitiveVersion;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelStrategy extends AbstractStrategy {


    public Optional<Field> recursionObtainVersionField(Optional<Class<?>> clazz)  throws Throwable {

        if (!clazz.isPresent())
            return Optional.empty();

        Optional<Field> existed = Arrays.stream(clazz.get().getDeclaredFields())
                .filter(field -> {
                    SensitiveVersion annotation = field.getAnnotation(SensitiveVersion.class);
                    Optional<SensitiveVersion> optional = Optional.ofNullable(annotation);
                    return optional.isPresent();
                }).findFirst();

        if (existed.isPresent())
            return existed;
        else
            return recursionObtainVersionField(Optional.ofNullable(clazz.get().getSuperclass()));

    }

    /**
     * 尾递归
     * @param data
     * @param clazz
     * @param purpose
     * @param boundedVersion
     */
    public void recursionOperateSensitiveField(Method method, Object data, Class clazz ,Purpose purpose,Integer boundedVersion) throws Throwable{
        if (clazz == null)
            return;

        // 为了把异常抛出来，不使用函数
        for (Field field : clazz.getDeclaredFields()) {
            Sensitive sensitive = field.getAnnotation(Sensitive.class);

            if (Optional.ofNullable(sensitive).isPresent()) {
                if (versionBounded(sensitive, boundedVersion)) {
                    field.setAccessible(Boolean.TRUE);
                    Object value = field.get(data);
                    Message message = Message.of(method,value, purpose,Optional.of(sensitive) ,IGNORED);
                    field.set(data, StrategyMediator.mediator.communicate(message));
                }
            }
        }
        recursionOperateSensitiveField(method,data,clazz.getSuperclass(),purpose,boundedVersion);

    }

    public boolean versionBounded(Sensitive sensitive ,Integer boundedVersion) {

        if (boundedVersion == null || boundedVersion == 0){
            return sensitive.version() <= 0;
        } else {
            return sensitive.version() <= boundedVersion;
        }
    }


    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) throws Throwable {

        Optional<Field> option = recursionObtainVersionField(Optional.of(data.getClass()));
        if (option.isPresent()){
            //该Model是敏感的
            Field field = option.get();
            field.setAccessible(Boolean.TRUE);
            Integer boundedVersion = (Integer) field.get(data);
            recursionOperateSensitiveField(metaData.method,data,data.getClass(),purpose,boundedVersion);
        }
        return data;
    }
}
