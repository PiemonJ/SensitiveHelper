package org.example;

public interface Monoid<T> {

    T unit();

    T op(T left,T right);
}
