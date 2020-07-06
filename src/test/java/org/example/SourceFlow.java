package org.example;


import io.reactivex.Observable;
import io.reactivex.functions.Function;



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

    public <O,I> BiGroupedFlow<O,I,T> BiGroup(Function<T,Pair<O,I>> groupFunction){

        return new BiGroupedFlow<>(dataFlow.groupBy(groupFunction));
    }

    public <O,I> BiGroupedFlow<O,I,T> BiGroup(Function<T,O> outerFunction,Function<T,I> innerFunction){

        return BiGroup(data -> new Pair<>(outerFunction.apply(data),innerFunction.apply(data)));
    }

}
