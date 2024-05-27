package com.binzaijun.stock.domain;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class StockChange {

    @JSONField(name = "n")
    @TableField("stock_name")
    private String stockName;
    @JSONField(name = "c")
    @TableField("stock_symbol")
    private String stockSymbol;
    @JSONField(name = "tm")
    @TableField(exist = false)
    private String tm;
    @JSONField(name = "t")
    @TableField(exist = false)
    private int type;
    @TableField("change_type")
    private String changeType;
    @JSONField(name = "i")
    @TableField("change_info")
    private String info;
    @TableField("change_date")
    private Date date;
    @TableId
    private String id;

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

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public StockChange(String stockName, String stockSymbol, String tm, String changeType, String info, Date date) {
        this.stockName = stockName;
        this.stockSymbol = stockSymbol;
        this.tm = tm;
        this.changeType = changeType;
        this.info = info;
        this.date = date;
    }

    @Override
    public String toString() {
        return "StockChange{" +
                "stockName='" + stockName + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", changeType='" + changeType + '\'' +
                ", info='" + info + '\'' +
                ", date=" + date +
                '}';
    }
}
