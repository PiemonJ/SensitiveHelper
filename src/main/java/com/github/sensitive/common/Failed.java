package com.github.sensitive.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Setter
@Getter
@AllArgsConstructor
public class Failed<R> extends Try<R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Throwable exception;

    @Override
    public R eval() throws Throwable {
        throw exception;
    }

}
