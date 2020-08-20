package com.github.sensitive.plugin.strategy;


import com.github.sensitive.common.Either;
import com.github.sensitive.common.Try;
import com.github.sensitive.enums.TypeKind;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractStrategy implements Strategy {

    public static final List<TypeKind> IGNORED = Arrays.asList(TypeKind.MAP);


    @Override
    public Try<Object> action(Message message) {
        try {
            return Try.success(doAction(message));
        } catch (Throwable e){
            return Try.failed(e);
        }
    }

    protected abstract Object doAction(Message message) throws Throwable;
}
