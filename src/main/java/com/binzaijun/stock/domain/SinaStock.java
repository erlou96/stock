package com.binzaijun.stock.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新浪股票数据接口类
 */
public class SinaStock {
    private Date day;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Long volume;
    @JSONField(name = "ma_price5")
    private BigDecimal maPrice5;
    @JSONField(name = "ma_volume5")
    private BigDecimal maVolume5;
    @JSONField(name = "ma_price10")
    private BigDecimal maPrice10;
    @JSONField(name = "ma_volume10")
    private BigDecimal maVolume10;
    @JSONField(name = "ma_price20")
    private BigDecimal maPrice20;
    @JSONField(name = "ma_volume20")
    private BigDecimal maVolume20;

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getMaPrice5() {
        return maPrice5;
    }

    public BigDecimal getMaVolume5() {
        return maVolume5;
    }

    public void setMaVolume5(BigDecimal maVolume5) {
        this.maVolume5 = maVolume5;
    }

    public BigDecimal getMaVolume10() {
        return maVolume10;
    }

    public void setMaVolume10(BigDecimal maVolume10) {
        this.maVolume10 = maVolume10;
    }

    public BigDecimal getMaVolume20() {
        return maVolume20;
    }

    public void setMaVolume20(BigDecimal maVolume20) {
        this.maVolume20 = maVolume20;
    }

    public void setMaPrice5(BigDecimal maPrice5) {
        this.maPrice5 = maPrice5;
    }

    public BigDecimal getMaPrice10() {
        return maPrice10;
    }

    public void setMaPrice10(BigDecimal maPrice10) {
        this.maPrice10 = maPrice10;
    }

    public BigDecimal getMaPrice20() {
        return maPrice20;
    }

    public void setMaPrice20(BigDecimal maPrice20) {
        this.maPrice20 = maPrice20;
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

    @Override
    public String toString() {
        return "SinaStockTest{" +
                "day=" + day +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", maPrice5=" + maPrice5 +
                ", maPrice10=" + maPrice10 +
                ", maPrice20=" + maPrice20 +
                '}';
    }
}