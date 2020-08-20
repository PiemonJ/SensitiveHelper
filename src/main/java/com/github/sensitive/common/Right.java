package com.github.sensitive.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Setter
@Getter
@AllArgsConstructor
public class Right<L, R> extends Either<L, R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final R value;

    @Override
    public R getRight() {
        return value;
    }

    @Override
    public L getLeft() {
        throw new NoSuchElementException("getLeft() on Right");
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }
}
