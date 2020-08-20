package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.Sensitive;
import com.github.sensitive.annotation.SensitiveVersion;
import com.github.sensitive.enums.Purpose;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ModelStrategy extends AbstractStrategy {


    @Override
    protected Object doAction(Message message) throws Throwable {
        Object data = message.getPayload();
        Optional<Field> version = recursionObtainVersionField(Optional.of(data.getClass()));
        if (version.isPresent()) {
            //该Model是敏感的
            Field versionField = version.get();
            versionField.setAccessible(Boolean.TRUE);
            Integer boundedVersion = (Integer) versionField.get(data);
            recursionOperateSensitiveField(message.getMethod(), data, data.getClass(), message.getPurpose(), boundedVersion);
        }
        return data;
    }

    /**
     * 递归获取版本控制字段
     * @param clazz
     * @return
     * @throws Throwable
     */
    public Optional<Field> recursionObtainVersionField(Optional<Class<?>> clazz) throws Throwable {

        if (!clazz.isPresent()) {
            return Optional.empty();
        }

        Optional<Field> existed = Arrays.stream(clazz.get().getDeclaredFields())
                .filter(field -> Optional.ofNullable(field.getAnnotation(SensitiveVersion.class)).isPresent())
                .findFirst();

        return existed.isPresent()
                ? existed
                : recursionObtainVersionField(Optional.ofNullable(clazz.get().getSuperclass()));

    }

    /**
     * 版本限制
     * @param sensitive
     * @param boundedVersion
     * @return
     */
    public boolean versionBounded(Sensitive sensitive, Integer boundedVersion) {

        if (boundedVersion == null || boundedVersion == 0) {
            return sensitive.version() <= 0;
        } else {
            return sensitive.version() <= boundedVersion;
        }
    }

    /**
     * 尾递归
     *
     * @param data
     * @param clazz
     * @param purpose
     * @param boundedVersion
     */
    public void recursionOperateSensitiveField(Method method, Object data, Class<?> clazz, Purpose purpose, Integer boundedVersion) throws Throwable {
        if (clazz == null) {
            return;
        }
        // 为了把异常抛出来，不使用函数
        for (Field field : clazz.getDeclaredFields()) {
            Sensitive sensitive = field.getAnnotation(Sensitive.class);

            if (Optional.ofNullable(sensitive).isPresent()) {
                if (versionBounded(sensitive, boundedVersion)) {
                    field.setAccessible(Boolean.TRUE);
                    Object value = field.get(data);
                    Message message = Message.of(method, value, purpose, Optional.of(sensitive), IGNORED);
                    field.set(data, StrategyMediator.mediator.communicate(message).eval());
                }
            }
        }
        recursionOperateSensitiveField(method, data, clazz.getSuperclass(), purpose, boundedVersion);

    }
}
