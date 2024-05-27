package com.binzaijun.stock.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.*;
import com.binzaijun.stock.service.StockService;
import com.binzaijun.stock.service.WatchListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

    private Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    StockService stockService;

    @Autowired
    WatchListService watchListService;

    /**
     * 首页展示
     */
    @GetMapping(value = "/index")
    public AjaxResult getStockInfoDTO(@RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize,
                                      @RequestParam(defaultValue = "") String stockName,
                                      @RequestParam(defaultValue = "consecutive_days_limit_up") String orderByColumn,
                                      @RequestParam(defaultValue = "isAsc") String isAsc) {
        if(null == pageNum || pageNum <= 1) {
            pageNum = 1L;
        }
        if(null == pageSize || pageSize <= 10) {
            pageSize = 10L;
        }
        Page<StockInfoDTO> stockInfoDTO = stockService.selectStockInfoDTO(pageNum, pageSize, stockName, orderByColumn, isAsc);
        return AjaxResult.success(stockInfoDTO);
    }

    /**
     * 添加stock_info_dto 数据
     */
    @GetMapping(value = "/add/{stockSymbol}")
    public AjaxResult addStockInfoDTO(@PathVariable(value = "stockSymbol") String stockSymbol) {

        Long existStockInfoDTO = stockService.isExistStockInfoDTO(stockSymbol);
        if (existStockInfoDTO != 0) {
            return AjaxResult.error("该条数据已存在!");
        }
        int insert = stockService.addStockInfoDTO(stockSymbol);
        return insert == 1 ? AjaxResult.success("插入成功") : AjaxResult.error("新增stockInfoDTO 表失败");
    }

    /**
     * 删除 stock_info_dto 数据
     */
    @DeleteMapping(value = "/delete/{stockSymbol}")
    public AjaxResult delStockInfoDTO(@PathVariable(value = "stockSymbol") String stockSymbol) {

        Long existStockInfoDTO = stockService.isExistStockInfoDTO(stockSymbol);
        if (existStockInfoDTO == 0) {
            return AjaxResult.error("该数据不存在!");
        }
        int delete = stockService.delStockInfoDTO(stockSymbol);
        return delete == 1 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @GetMapping(value = "/update")
    public AjaxResult updateStockInfo() {

        List<StockInfoDTO> stockInfoDTOS = stockService.selectStockInfoDTO();

        return stockService.updateBatchStockInfoDTO(stockInfoDTOS);
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @GetMapping(value = "/kline/{symbol}")
    public AjaxResult getStockKLine(@PathVariable(value = "symbol") String stockSymbol) {

        logger.info("获取{}的k线数据",stockSymbol);
        Long existStockInfo = stockService.isExistStockInfo(stockSymbol);
        if (existStockInfo == 0) {
            return AjaxResult.error("该数据不存在!");
        }

        StockKLineDateDTO stockKLineDateDTO = stockService.getStockKLineBySymbol(stockSymbol);

        return AjaxResult.success(stockKLineDateDTO);
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @GetMapping(value = "/search")
    public AjaxResult getStockInfoByES(String queryString) {

        List<StockInfo> stockInfoList = stockService.getStockInfoByES(queryString);
        return AjaxResult.success(stockInfoList);
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @PostMapping(value = "/current-price")
    public AjaxResult getStockCurrentPrice(@RequestBody List<String> stockSymbols) {
        if (stockSymbols.size() == 0) {
            return AjaxResult.error("传入参数错误");
        }
        List<QtStock> stockCurrentPrice = stockService.getStockCurrentPrice(stockSymbols);
        return AjaxResult.success(stockCurrentPrice);
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @GetMapping(value = "/select")
    public AjaxResult getStockInfoDTOByStockName(@RequestParam String stockSymbols) {

        List<StockInfoDTO> stockInfoDTOS = stockService.getStockInfoDTOByStockName(stockSymbols);
        return AjaxResult.success(stockInfoDTOS);
    }

    /**
     * 获取东方财富异动信息，添加到数据库中
     */
    @GetMapping(value = "/add-change-info")
    public AjaxResult addStockChange() {

        boolean b = stockService.saveStockChange();
        if (b) {
            return AjaxResult.success("添加成功");
        }else {
            return AjaxResult.error("添加失败");
        }

    }

    /**
     * 获取东方财富异动信息，添加到数据库中
     */
    @GetMapping(value = "/get-change-info")
    public AjaxResult getStockChange(@RequestParam(value = "changeType", defaultValue = "8202") int[] changeType) {

        logger.info("获取异动类型：" + Arrays.toString(changeType));
        List<StockChange> stockChange = stockService.getAllStockChange(changeType);
        return AjaxResult.success(stockChange);
        
    }


}
