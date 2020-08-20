package com.github.sensitive.plugin.strategy;

import com.github.sensitive.common.Either;
import com.github.sensitive.common.Pair;
import com.github.sensitive.common.SensitiveMetadataEntry;
import com.github.sensitive.common.Try;
import com.github.sensitive.enums.Purpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public interface Strategy {

    ArrayStrategy ARRAY_STRATEGY = new ArrayStrategy();

    ListStrategy LIST_STRATEGY = new ListStrategy();

    MapStrategy MAP_STRATEGY = new MapStrategy();

    StringStrategy STRING_STRATEGY = new StringStrategy();

    ModelStrategy MODEL_STRATEGY = new ModelStrategy();

    Try<Object> action(Message message) throws Throwable;

}
