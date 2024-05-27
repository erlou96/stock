package com.binzaijun.stock.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.StockWatchList;
import com.binzaijun.stock.service.StockService;
import com.binzaijun.stock.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/watchlist")
public class WatchListController {

    @Autowired
    WatchListService watchListService;

    @Autowired
    StockService stockService;


    @GetMapping(value = "/list")
    public AjaxResult listStockWatchList(Long pageNum, Long pageSize) {
        if(null == pageNum || pageNum <= 1) {
            pageNum = 1L;
        }
        if(null == pageSize || pageSize <= 10) {
            pageSize = 10L;
        }
        Page<StockWatchList> stockWatchListPage = watchListService.pageStockWatchList(pageNum, pageSize);
        return AjaxResult.success(stockWatchListPage);
    }
}
