package org.example;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import org.checkerframework.checker.units.qual.K;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class SourceFlow<T> {

    Observable<T> dataFlow;

    public static <T> SourceFlow<T> from(Observable<T> dataFlow){
        return new SourceFlow<>(dataFlow);
    }

    public static <T> SourceFlow<T> from(T ... datas){
        return new SourceFlow<>(
                Observable.fromArray(datas)
        );
    }

    public static <T> SourceFlow<T> from(Iterable<? extends T> iterable){
        return new SourceFlow<>(
                Observable.fromIterable(iterable)
        );
    }


    private SourceFlow(Observable<T> dataFlow) {
        this.dataFlow = dataFlow;
    }

    public <K> GroupedFlow<K,T> Group(Function<T,K> groupFunction){
        return new GroupedFlow<>(dataFlow.groupBy(groupFunction));
    }




}
