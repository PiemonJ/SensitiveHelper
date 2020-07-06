package org.example;


import io.reactivex.functions.Function;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 */
public class Magic {

    public static <T> SourceFlow<T> from(Iterable<? extends T> datas){
        return SourceFlow.from(datas);
    }

    public static <T> SourceFlow<T> from(T ... datas){
        return SourceFlow.from(datas);
    }

    public static void main(String[] args) {


        Data one = new Data("一班", "2020-07-03", "迟到", BigDecimal.ONE, BigDecimal.ONE);
        Data two = new Data("一班", "2020-07-04", "迟到", BigDecimal.ONE, BigDecimal.TEN);
        Data three = new Data("二班", "2020-07-04", "迟到", BigDecimal.ONE, BigDecimal.ONE);
        Data four = new Data("二班", "2020-07-05", "不迟到", BigDecimal.ONE, BigDecimal.ONE);


        // 双分组
        SinkFlow<Pair<Pair<String, String>, Metric>> sink1 = Magic.from(one, two, three, four)
                .BiGroup(Data::getPsm, Data::getRank)
                .MapReduce(data -> Metric.of(data.getWeight(), data.getValue(), data.getValue(), data.getValue()), Metric.METRIC_MONOID)
                .Sink(x -> x);

        Map<Pair<String, String>, BigDecimal> pairBigDecimalMap =
                sink1.CollectMap(Pair::getKey, kv -> kv.getValue().getValue().divide(kv.getValue().getWeight()));




        // 单分组
        SinkFlow<Pair<String, Metric>> sink = Magic.from(one, two, three, four)
                .Group(Data::getRank)
                .MapReduce(data -> Metric.of(data.getWeight(), data.getValue(), data.getValue(), data.getValue()), Metric.METRIC_MONOID)
                .Sink(x -> x);

        Map<String, BigDecimal> kvmap = sink.CollectMap(Pair::getKey, kv -> kv.getValue().getValue().divide(kv.getValue().getWeight()));

        List<Pair<String, Metric>> pairs = sink.CollectList(x -> x);
    }
}
