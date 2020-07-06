package org.example;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class ReducedFlow<K, R> {

    Observable<Pair<K, R>> reducedDataFlow;

    public ReducedFlow(Observable<Pair<K, R>> reducedDataFlow) {
        this.reducedDataFlow = reducedDataFlow;
    }

    public <T> SinkFlow<Pair<K,T>> Sink(BiFunction<K, R, T> biFunction) {
        return new SinkFlow<>(
                reducedDataFlow.map(
                        krPair -> new Pair<>(krPair.getKey(),biFunction.apply(krPair.getKey(), krPair.getValue()))
                )
        );
    }

    public <T> SinkFlow<Pair<K,T>> Sink(Function<R, T> biFunction) {
        return new SinkFlow<>(
                reducedDataFlow.map(
                        krPair -> new Pair<>(krPair.getKey(),biFunction.apply(krPair.getValue()))
                )
        );
    }

}
