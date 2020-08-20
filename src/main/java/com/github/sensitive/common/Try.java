package com.github.sensitive.common;

public abstract class Try<R> {


    public static <R> Try<R> failed(Throwable exception) {
        return new Failed<>(exception);
    }

    public static <R> Try<R> success(R right) {
        return new Success<>(right);
    }

    public abstract R eval() throws Throwable;
}
