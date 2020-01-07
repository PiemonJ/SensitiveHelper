package com.github.sensitive.plugin.strategy;
import com.github.sensitive.common.Constant;
import com.github.sensitive.enums.Purpose;
import com.github.sensitive.enums.TypeKind;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ListStrategy extends AbstractStrategy{


    @Override
    public Object action(Object data, MetaData metaData, Purpose purpose) throws Throwable {
        List collection = (List) data;
        for (int index = 0; index < collection.size(); index++) {
            Object element = collection.get(index);
            Message message = Message.of(metaData.method, element, purpose, Optional.of(Constant.ANY), IGNORED);
            collection.set(index, StrategyMediator.mediator.communicate(message));
        }
        return data;
    }
}
