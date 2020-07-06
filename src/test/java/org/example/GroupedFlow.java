package org.example;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;


public class GroupedFlow<K, T> {

    Observable<GroupedObservable<K, T>> groupedDataFlow;

    public GroupedFlow(Observable<GroupedObservable<K, T>> groupedDataFlow) {
        this.groupedDataFlow = groupedDataFlow;
    }

    public <S> ReducedFlow<K, S> MapReduce(Function<T, S> mapping, Monoid<S> reducer) {

        return new ReducedFlow<>(
                groupedDataFlow.flatMapSingle(
                        group ->
                                group.map(mapping)
                                        .reduce(reducer.unit(), reducer::op)
                                        .map(reduce -> new Pair<>(group.getKey(), reduce))

                )
        );
    }

    public <S> ReducedFlow<K, T> MapReduce(Monoid<T> reducer) {

        return new ReducedFlow<>(
                groupedDataFlow.flatMapSingle(
                        group -> group.reduce(reducer.unit(), reducer::op)
                                .map(reduce -> new Pair<>(group.getKey(), reduce))

                )
        );
    }

}
