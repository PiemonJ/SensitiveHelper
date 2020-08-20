package com.github.sensitive.plugin.strategy;

import com.github.sensitive.annotation.*;
import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public class MapStrategy extends AbstractStrategy {


    @Override
    protected Object doAction(Message message) throws Throwable {
        Object data = message.getPayload();
        Map<String, Object> map = (Map<String, Object>) data;
        Annotation annotation = message.getAnnotation().get();
        assert annotation instanceof SensitiveMap;
        SensitiveMap sensitiveMap = (SensitiveMap) annotation;

        for (SensitiveEntry sensitiveEntry : sensitiveMap.entries()) {
            String key = sensitiveEntry.key();
            Object value = map.get(key);
            Message command = Message.of(message.getMethod(), value, message.getPurpose(), Optional.of(sensitiveEntry), IGNORED);
            Object result = StrategyMediator.mediator.communicate(command).eval();
            map.put(key,result);
        }

        return data;
    }
}
