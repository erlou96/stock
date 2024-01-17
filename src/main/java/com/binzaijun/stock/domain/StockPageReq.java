package com.binzaijun.stock.domain;

public class StockPageReq {

    /** 股票编号 **/
    private String symbol;

    /** 当前页数，从1开始 **/
    private Integer current;

    /** 每页记录数 **/
    private Integer size;

    public StockPageReq() {
        current = 1;
        size = 10;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}