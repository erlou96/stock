package com.binzaijun.stock.domain;

import java.math.BigDecimal;
import java.util.Date;

public class SinaStock {
    private String symbol;
    private String name;
    private Date day;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Integer volume;
    private BigDecimal MaPrice5;
    private Integer MaVolume5;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public BigDecimal getMaPrice5() {
        return MaPrice5;
    }

    public void setMaPrice5(BigDecimal maPrice5) {
        MaPrice5 = maPrice5;
    }

    public Integer getMaVolume5() {
        return MaVolume5;
    }

    public void setMaVolume5(Integer maVolume5) {
        MaVolume5 = maVolume5;
    }
}
