package com.binzaijun.stock.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.StockInfo;
import com.binzaijun.stock.domain.StockWatchList;
import com.binzaijun.stock.mapper.StockInfoDTOMapper;
import com.binzaijun.stock.mapper.StockInfoMapper;
import com.binzaijun.stock.mapper.WatchListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchListService {

    @Autowired
    private WatchListMapper watchListMapper;

    @Autowired
    private StockInfoMapper stockInfoMapper;

    @Autowired
    private StockInfoDTOMapper stockInfoDTOMapper;

    /**
     * 添加自选股
     * @param stockSymbol
     */
    public AjaxResult saveStockWatchList(String stockSymbol) {
        // 判断是否已经存在
        QueryWrapper<StockWatchList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_symbol", stockSymbol);
        Long selectCount = watchListMapper.selectCount(queryWrapper);
        if(selectCount > 0) {
            return AjaxResult.error("数据库里已有此地址");
        }

        // 判断stockInfo 表中是否存在该数据
        StockInfo stockInfo = getOneStockWatchList(stockSymbol);


        if (stockInfo.getStockSymbol().equals(stockSymbol)) {
            StockWatchList stockWatchList = new StockWatchList();
            stockWatchList.setStockSymbol(stockSymbol);
            int save = watchListMapper.insert(stockWatchList);
            if (save != 1) {
                return AjaxResult.error("新增 watchList 表失败");
            }
        }

        return AjaxResult.success("添加成功");

    }


    public Page<StockWatchList> pageStockWatchList(Long pageNum, Long pageSize) {
        QueryWrapper<StockWatchList> queryWrapper = new QueryWrapper<>();
        Page<StockWatchList> page = new Page<>(pageNum, pageSize);
        Page<StockWatchList> stockWatchListPage = watchListMapper.selectPage(page, queryWrapper);
        return stockWatchListPage;
    }

    public List<StockWatchList> selectStockWatchList() {
        QueryWrapper<StockWatchList> queryWrapper = new QueryWrapper<>();
        List<StockWatchList> watchLists = watchListMapper.selectList(queryWrapper);
        return watchLists;
    }

    public StockInfo getOneStockWatchList(String symbol) {
        QueryWrapper<StockInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("stock_symbol", symbol);
        StockInfo stockInfo = stockInfoMapper.selectOne(queryWrapper);
        return stockInfo;
    }
}
