package com.binzaijun.stock.domain;

import java.util.List;

/**
 * 个股历史k线数据
 */
public class StockKLineDateDTO {
    private String stockName;
    private String stockSymbol;
    private List<SinaStock> sinaStockList;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public List<SinaStock> getSinaStockList() {
        return sinaStockList;
    }

    public void setSinaStockList(List<SinaStock> sinaStockList) {
        this.sinaStockList = sinaStockList;
    }

    @Override
    public String toString() {
        return "StockKLineDateDTO{" +
                "stockName='" + stockName + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", sinaStockList=" + sinaStockList +
                '}';
    }
}
