package com.github.sensitive.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Setter
@Getter
@AllArgsConstructor
public class Success<R> extends Try<R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final R value;

    @Override
    public R eval() throws Throwable {
        return value;
    }
}
