package com.binzaijun.stock.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "stock_info_dto")
public class StockInfoDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String stockSymbol;
    private String stockName;
    private Date HighestDate;
    private Date LowestDate;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private int consecutiveDaysLimitUp;
    private int daysLimitUp;
    private double volatility;
    // 行业
    private String industry;

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getHighestDate() {
        return HighestDate;
    }

    public void setHighestDate(Date highestDate) {
        HighestDate = highestDate;
    }

    public Date getLowestDate() {
        return LowestDate;
    }

    public void setLowestDate(Date lowestDate) {
        LowestDate = lowestDate;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public int getConsecutiveDaysLimitUp() {
        return consecutiveDaysLimitUp;
    }

    public void setConsecutiveDaysLimitUp(int consecutiveDaysLimitUp) {
        this.consecutiveDaysLimitUp = consecutiveDaysLimitUp;
    }

    public int getDaysLimitUp() {
        return daysLimitUp;
    }

    public void setDaysLimitUp(int daysLimitUp) {
        this.daysLimitUp = daysLimitUp;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }


    @Override
    public String toString() {
        return "StockInfoDTO{" +
                "id=" + id +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", HighestDate=" + HighestDate +
                ", LowestDate=" + LowestDate +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                ", consecutiveDaysLimitUp=" + consecutiveDaysLimitUp +
                ", daysLimitUp=" + daysLimitUp +
                ", volatility=" + volatility +
                ", industry='" + industry + '\'' +
                '}';
    }
}
