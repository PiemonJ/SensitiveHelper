package org.example;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class BiGroupedFlow<O, I, T> {

    Observable<GroupedObservable<Pair<O, I>, T>> groupedDataFlow;

    public BiGroupedFlow(Observable<GroupedObservable<Pair<O, I>, T>> groupedDataFlow) {
        this.groupedDataFlow = groupedDataFlow;
    }

    public <S> ReducedFlow<Pair<O, I>, S> MapReduce(Function<T, S> mapping, Monoid<S> reducer) {
        return new ReducedFlow<>(
                groupedDataFlow.flatMapSingle(
                        group ->
                                group.map(mapping)
                                        .reduce(reducer.unit(), reducer::op)
                                        .map(reduce -> new Pair<>(group.getKey(), reduce))

                )
        );
    }

    public <S> ReducedFlow<Pair<O, I>, T> MapReduce(Monoid<T> reducer) {
        return new ReducedFlow<>(
                groupedDataFlow.flatMapSingle(
                        group ->
                                group.reduce(reducer.unit(), reducer::op)
                                        .map(reduce -> new Pair<>(group.getKey(), reduce))

                )
        );
    }
}
