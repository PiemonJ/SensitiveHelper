package org.example;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class SinkFlow<T> {

    Observable<T> sinkDataFlow;

    public SinkFlow(Observable<T> sinkDataFlow) {
        this.sinkDataFlow = sinkDataFlow;
    }

    public <V> List<V> CollectList(Function<T,V> mapping){
        return sinkDataFlow.map(mapping).toList().blockingGet();
    }

    public <V> List<V> CollectSortedList(Function<T,V> mapping, Comparator<? super V> comparator){
        return sinkDataFlow.map(mapping).toSortedList(comparator).blockingGet();
    }

    public <K,V> Map<K,V> CollectMap(Function<T,K> keySelector,Function<T,V> valueSelector){
        return sinkDataFlow.toMap(keySelector,valueSelector).blockingGet();
    }

}
