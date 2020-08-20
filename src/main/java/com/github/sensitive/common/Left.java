package com.github.sensitive.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Setter
@Getter
@AllArgsConstructor
public class Left<L, R> extends Either<L, R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final L value;

    @Override
    public R getRight() {
        throw new NoSuchElementException("getLeft() on Right");
    }

    @Override
    public L getLeft() {
        throw new NoSuchElementException("getLeft() on Right");
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

}
