package com.github.sensitive.plugin.strategy;

import com.github.sensitive.common.Constant;
import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ListStrategy extends AbstractStrategy {

    @Override
    protected Object doAction(Message message) throws Throwable {
        Object data = message.getPayload();
        assert data instanceof List;
        List collection = (List) data;
        for (int index = 0; index < collection.size(); index++) {
            Object element = collection.get(index);
            Message command = Message.of(message.getMethod(), element, message.getPurpose(), Optional.of(Constant.ANY), IGNORED);
            collection.set(index, StrategyMediator.mediator.communicate(command).eval());
        }
        return data;
    }
}
