package org.example;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.List;
import java.util.Map;


public class SinkFlow<T> {

    Observable<T> sinkDataFlow;

    public SinkFlow(Observable<T> sinkDataFlow) {
        this.sinkDataFlow = sinkDataFlow;
    }

    public <V> List<V> CollectList(Function<T,V> mapping){
        return sinkDataFlow.map(mapping).toList().blockingGet();
    }

    public <K,V> Map<K,V> CollectMap(Function<T,K> keySelector,Function<T,V> valueSelector){
        return sinkDataFlow.toMap(keySelector,valueSelector).blockingGet();

    }
}
