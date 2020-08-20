package com.github.sensitive.plugin.strategy;

import com.github.sensitive.common.Constant;
import com.github.sensitive.common.Either;
import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Optional;

public class ArrayStrategy extends AbstractStrategy {

    @Override
    protected Object doAction(Message message) throws Throwable {
        Object data = message.getPayload();
        for (int index = 0; index < Array.getLength(data); index++) {
            Object element = Array.get(data, index);
            Message command = Message.of(message.getMethod(), element, message.getPurpose(), Optional.of(Constant.ANY), IGNORED);
            Array.set(data,index,StrategyMediator.mediator.communicate(command).eval());
        }
        return data;
    }
}
