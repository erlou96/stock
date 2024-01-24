package com.binzaijun.stock.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.*;
import com.binzaijun.stock.mapper.StockInfoDTOMapper;
import com.binzaijun.stock.mapper.StockInfoMapper;
import com.binzaijun.stock.mapper.WatchListMapper;
import com.binzaijun.stock.util.EsUtil;
import com.binzaijun.stock.util.SinaStockDataUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockInfoDTOMapper stockInfoDTOMapper;

    @Autowired
    private StockInfoMapper stockInfoMapper;

    @Autowired
    private WatchListService watchListService;


    /**
     * 根据股票代码获取历史k线数据
     */
    public StockKLineDateDTO getStockKLineBySymbol(String symbol){

        StockKLineDateDTO stockKLineDateDTO = new StockKLineDateDTO();

        stockKLineDateDTO.setStockSymbol(symbol);

        StockInfo oneStockInfo = getOneStockInfo(symbol);

        stockKLineDateDTO.setStockName(oneStockInfo.getStockName());

        // 获取sina 数据
        List<SinaStock> sinaStockList = SinaStockDataUtil.getRequest(symbol);

        stockKLineDateDTO.setSinaStockList(sinaStockList);

        return stockKLineDateDTO;
    }

    /**
     * 分页查询
     * @return
     */
    public Page<StockInfoDTO> getStockInfoDTO(Long pageNum, Long pageSize, String stockName, String orderByColumn, String isAsc) {

        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper<>();

        orderByColumn = StrUtil.toUnderlineCase(orderByColumn);

        if ("asc".equals(isAsc)) {
            queryWrapper.orderByAsc(orderByColumn);
        }else {
            queryWrapper.orderByDesc(orderByColumn);
        }

        if (!stockName.equals("")) {
            queryWrapper.eq("stock_name", stockName);
        }


        Page<StockInfoDTO> page = new Page<>(pageNum, pageSize);

        Page<StockInfoDTO> data = stockInfoDTOMapper.selectPage(page, queryWrapper);

        return data;
    }

    public List<StockInfoDTO> getStockInfoDTOByStockName(String stockName) {

        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("stock_name", stockName);

        List<StockInfoDTO> stockInfoDTOS = stockInfoDTOMapper.selectList(queryWrapper);

        return stockInfoDTOS;
    }

    public Long isExistStockInfoDTO(String stockSymbol) {
        // 判断是否已经存在
        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_symbol", stockSymbol);
        Long selectCount = stockInfoDTOMapper.selectCount(queryWrapper);
        return selectCount;
    }

    public Long isExistStockInfo(String stockSymbol) {
        // 判断是否已经存在
        QueryWrapper<StockInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_symbol", stockSymbol);
        Long selectCount = stockInfoMapper.selectCount(queryWrapper);
        return selectCount;
    }

    /**
     * 新增
     * @return
     */
    public int addStockInfoDTO(String stockSymbol) {
        StockInfo stockInfo = getOneStockInfo(stockSymbol);
        // 添加 stockInfoDTO 数据
        StockInfoDTO stockInfoDTO = new StockInfoDTO();
        stockInfoDTO.setStockSymbol(stockSymbol);
        stockInfoDTO = SinaStockDataUtil.construct(stockInfoDTO);
        stockInfoDTO.setIndustry(stockInfo.getIndustry());
        stockInfoDTO.setStockName(stockInfo.getStockName());
        stockInfoDTO.setStockSymbol(stockInfo.getStockSymbol());

        // 插入到 stockInfoDTO 表中
        int insert = stockInfoDTOMapper.insert(stockInfoDTO);

        return insert;
    }


    public int delStockInfoDTO(String stockSymbol) {
        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_symbol", stockSymbol);
        // 插入到 stockInfoDTO 表中
        int delete = stockInfoDTOMapper.delete(queryWrapper);
        return delete;
    }

    /**
     * 查询单条 stock_info 数据
     * @param stockSymbol
     * @return
     */
    public StockInfo getOneStockInfo(String stockSymbol) {
        QueryWrapper<StockInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("stock_symbol", stockSymbol);
        StockInfo stockInfo = stockInfoMapper.selectOne(queryWrapper);
        return stockInfo;
    }

    public List<StockInfo> selectStockInfo(List<String> stockSymbolList) {
        QueryWrapper<StockInfo> queryWrapper = new QueryWrapper();
        queryWrapper.in("stock_symbol", stockSymbolList);
        List<StockInfo> stockInfo = stockInfoMapper.selectList(queryWrapper);
        return stockInfo;
    }



    /**
     * 查询单条 stock_info_dto 数据
     * @param
     * @return
     */
    public List<StockInfoDTO> selectStockInfoDTO() {
        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper();
        List<StockInfoDTO> stockInfoDTOS = stockInfoDTOMapper.selectList(queryWrapper);
        return stockInfoDTOS;
    }

    /**
     * 查询条数 stock_info_dto 数据
     * @param stockSymbol
     * @return
     */
    public Long countStockInfoDTO(String stockSymbol) {
        QueryWrapper<StockInfoDTO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("stock_symbol", stockSymbol);
        Long count = stockInfoDTOMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 更新单条 stock_info_dto 数据
     * @param stockSymbol
     * @return
     */
    public AjaxResult updateStockInfoDTO(String stockSymbol) {
        StockInfo stockInfo = getOneStockInfo(stockSymbol);
        StockInfoDTO stockInfoDTO = new StockInfoDTO();
        stockInfoDTO.setStockSymbol(stockSymbol);
        stockInfoDTO = SinaStockDataUtil.construct(stockInfoDTO);
        stockInfoDTO.setIndustry(stockInfo.getIndustry());
        stockInfoDTO.setStockName(stockInfo.getStockName());
        stockInfoDTO.setStockSymbol(stockSymbol);

        UpdateWrapper<StockInfoDTO> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("stock_symbol", stockSymbol);
        int update = stockInfoDTOMapper.update(stockInfoDTO, updateWrapper);
        return update == 1 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    public AjaxResult updateBatchStockInfoDTO(List<StockInfoDTO> stockInfos) {

        stockInfos.stream().forEach(
                stockInfoDTO -> {
                    StockInfoDTO construct = SinaStockDataUtil.construct(stockInfoDTO);
                    stockInfoDTOMapper.updateById(construct);
                }
        );

//        stockInfos.stream().forEach(stockInfoDTO -> {
//            System.out.println(stockInfoDTO.getHighestPrice() + "  " + stockInfoDTO.getVolatility());
//            UpdateWrapper<StockInfoDTO> updateWrapper = new UpdateWrapper();
//            updateWrapper.eq("stock_symbol", stockInfoDTO.getStockSymbol());
//            int update = stockInfoDTOMapper.update(stockInfoDTO, updateWrapper);
//            if (update == 0) {
//                stockInfoDTOMapper.insert(stockInfoDTO);
//            }
//        });
        return  AjaxResult.success("更新成功");
    }

    public List<StockInfo> getStockInfoByES(String queryString) {

        SearchResponse searchResponse = null;

        if (queryString == "") {
             searchResponse = EsUtil.selectAllData();
        }else {
             searchResponse = EsUtil.selectData(queryString);
        }

        List<StockInfo> stockInfoList = new ArrayList<>();

        if (searchResponse != null) {
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            for (SearchHit hit : searchHits) {
                String documentId = hit.getId();
                String sourceAsString = hit.getSourceAsString();
                StockInfo stockInfo = JSONObject.parseObject(sourceAsString, StockInfo.class);
                // 处理您的文档数据
                System.out.println("Document ID: " + documentId);
                System.out.println("Document Source: " + sourceAsString);
                stockInfoList.add(stockInfo);
            }
        }

        return stockInfoList;
    }

    public List<QtStock> getStockCurrentPrice(List<String> stockSymbol) {
        List<QtStock> stockInfoRealTime = SinaStockDataUtil.getStockInfoRealTime(stockSymbol);
        return stockInfoRealTime;
    }

    public StockKLineDateDTO getStockKLineBySymbolTest(String stockSymbol) {
        List<SinaStock> request = SinaStockDataUtil.getRequest(stockSymbol);
        StockKLineDateDTO stockKLineDateDTO = new StockKLineDateDTO();

        return null;
    }
}
