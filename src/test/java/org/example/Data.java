package org.example;

import java.math.BigDecimal;

public class Data {

    String psm;

    String date;

    String rank;

    BigDecimal weight;

    BigDecimal value;

    public Data(String psm, String date, String rank, BigDecimal weight, BigDecimal value) {
        this.psm = psm;
        this.date = date;
        this.rank = rank;
        this.weight = weight;
        this.value = value;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getPsm() {
        return psm;
    }

    public void setPsm(String psm) {
        this.psm = psm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
