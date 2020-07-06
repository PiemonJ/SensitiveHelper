package org.example;

import java.math.BigDecimal;

public class Metric {

    public static final Monoid<Metric> METRIC_MONOID = new Monoid<Metric>() {
        @Override
        public Metric unit() {
            return Metric.zero();
        }

        @Override
        public Metric op(Metric left, Metric right) {
            return new Metric(
                    left.getWeight().add(right.getWeight()),
                    left.getValue().add(right.getValue()),
                    left.getMax().max(right.getMax()),
                    left.getMin().min(right.getMin())
            );
        }
    };

    /**
     * 权重值
     */
    BigDecimal weight;

    /**
     * rank的值
     */
    BigDecimal value;

    /**
     * rank的最大值,特定于分组键
     */
    BigDecimal max;

    /**
     * rank的最小值,特定于分组键
     */
    BigDecimal min;


    public static Metric of(BigDecimal weight, BigDecimal value, BigDecimal max, BigDecimal min) {
        return new Metric(weight, value, max, min);
    }

    public static Metric of(BigDecimal weight, BigDecimal value) {
        return new Metric(weight, value, value, value);
    }

    public static Metric zero() {
        return new Metric(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public Metric(BigDecimal weight, BigDecimal value, BigDecimal max, BigDecimal min) {
        this.weight = weight;
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
