package com.binzaijun.stock.domain;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@TableName(value = "stock_info")
public class StockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JSONField(name = "stock_symbol")
    private String stockSymbol;
    @JSONField(name = "stock_name")
    private String stockName;
    private String industry;
    @JSONField(name = "listing_date")
    private Date listingDate;
    @JSONField(name = "total_share_capital")
    private Long totalShareCapital;
    @JSONField(name = "circulating_shares")
    private Long circulatingShares;
    @JSONField(name = "stock_category")
    private String stockCategory;


    public String getStockCategory() {
        return stockCategory;
    }

    public void setStockCategory(String stockCategory) {
        this.stockCategory = stockCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getListingDate() {
        return listingDate;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public Long getTotalShareCapital() {
        return totalShareCapital;
    }

    public void setTotalShareCapital(Long totalShareCapital) {
        this.totalShareCapital = totalShareCapital;
    }

    public Long getCirculatingShares() {
        return circulatingShares;
    }

    public void setCirculatingShares(Long circulatingShares) {
        this.circulatingShares = circulatingShares;
    }
}
