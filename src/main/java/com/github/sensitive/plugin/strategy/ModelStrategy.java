package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.Sensitive;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ModelStrategy extends AbstractStrategy {


    public Optional<Field> recursionObtainField(Optional<Class<?>> clazz){

        if (!clazz.isPresent())
            return Optional.empty();

        Optional<Field> existed = Arrays.asList(clazz.get().getDeclaredFields())
                .stream()
                .filter(fieldName -> fieldName.getName().equals(GLOBAL_VERSION_NAME))
                .findFirst();

        if (existed.isPresent())
            return existed;
        else
            return recursionObtainField(Optional.ofNullable(clazz.get().getSuperclass()));

    }

    public void recursionOperateField(Object data, Class clazz ,Purpose purpose,Integer boundedVersion){
        if (clazz == null)
            return;

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> this.versionBounded(field,boundedVersion))
                .forEach(
                        field -> {
                            try {
                                field.setAccessible(Boolean.TRUE);
                                Object value = field.get(data);
                                field.set(
                                        data,
                                        StrategyPatternMatching.matching(value,null, purpose, TypeKind.MAP));

                            } catch (Exception e){
                            }
                        }

                );

        recursionOperateField(data,clazz.getSuperclass(),purpose,boundedVersion);

    }

    public boolean versionBounded(Field filed ,Integer boundedVersion){

        Optional<Sensitive> annotation = Optional.of(filed.getAnnotation(Sensitive.class));
        if (annotation.isPresent()){
            Sensitive sensitive = annotation.get();
            if (boundedVersion == null || boundedVersion == 0){
                return sensitive.version() <= 0;
            } else {
                return sensitive.version() <= boundedVersion;
            }
        }
        return false;

    }


    @Override
    public Object action(Object data, Annotation annotation, Purpose purpose) {

        Optional<Field> option = recursionObtainField(Optional.of(data.getClass()));
        if (option.isPresent()){
            Field field = option.get();
            field.setAccessible(Boolean.TRUE);
            try {
                Integer boundedVersion = (Integer) field.get(data);
                recursionOperateField(data,data.getClass(),purpose,boundedVersion);
            } catch (IllegalAccessException e) {

            }
        }

        return data;
    }
}
