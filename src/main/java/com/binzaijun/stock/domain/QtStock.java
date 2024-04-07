package com.binzaijun.stock.domain;

import java.math.BigDecimal;

/**
 * 腾讯股票接口数据类
 */
public class QtStock {
    // 股票代码
    private String stockSymbol;
    // 股票名称
    private String stockName;
    // 交易所
    private int exchange;
    // 当前价格
    private double currentPrice;
    // 涨跌金额
    private double change;
    // 涨跌幅度
    private double changePercentage;
    // 成交量
    private long volume;
    // 成交金额
    private double turnover;
    // 总市值
    private double marketValue;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        exchange = exchange;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public QtStock() {
    }

    public QtStock(String stockSymbol, String stockName, int exchange, double currentPrice, double change, double changePercentage, long volume, double turnover, double marketValue) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.exchange = exchange;
        this.currentPrice = currentPrice;
        this.change = change;
        this.changePercentage = changePercentage;
        this.volume = volume;
        this.turnover = turnover;
        this.marketValue = marketValue;
    }

    @Override
    public String toString() {
        return "QtStock{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", exchange=" + exchange +
                ", currentPrice=" + currentPrice +
                ", change=" + change +
                ", changePercentage=" + changePercentage +
                ", volume=" + volume +
                ", turnover=" + turnover +
                ", marketValue=" + marketValue +
                '}';
    }
}
