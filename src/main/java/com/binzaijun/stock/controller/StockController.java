package com.binzaijun.stock.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.*;
import com.binzaijun.stock.service.StockService;
import com.binzaijun.stock.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

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
        Page<StockInfoDTO> stockInfoDTO = stockService.getStockInfoDTO(pageNum, pageSize, stockName, orderByColumn, isAsc);
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
    public AjaxResult getStockKLine(@PathVariable String symbol) {
        List<SinaStock> stockKLineBySymbol = stockService.getStockKLineBySymbol(symbol);
        return AjaxResult.success(stockKLineBySymbol);
    }

    /**
     * 更新stock_info_dto 数据
     * @return
     */
    @GetMapping(value = "/search")
    public AjaxResult getStockInfoByES(String queryString) {

        System.out.println("sdf"+queryString);
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

}
