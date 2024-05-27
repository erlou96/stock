package com.binzaijun.stock.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.*;
import com.binzaijun.stock.mapper.StockChangeMapper;
import com.binzaijun.stock.mapper.StockInfoDTOMapper;
import com.binzaijun.stock.mapper.StockInfoMapper;
import com.binzaijun.stock.util.EsUtil;
import com.binzaijun.stock.util.StockDataUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Resource
    private StockInfoDTOMapper stockInfoDTOMapper;

    @Resource
    private StockInfoMapper stockInfoMapper;

    @Resource
    private StockChangeMapper stockChangeMapper;


    /**
     * 根据股票代码获取历史k线数据
     */
    public StockKLineDateDTO getStockKLineBySymbol(String symbol){

        StockKLineDateDTO stockKLineDateDTO = new StockKLineDateDTO();

        stockKLineDateDTO.setStockSymbol(symbol);

        StockInfo oneStockInfo = getOneStockInfo(symbol);

        stockKLineDateDTO.setStockName(oneStockInfo.getStockName());

        // 获取sina 数据
        List<SinaStock> sinaStockList = StockDataUtil.getRequest(symbol);

        stockKLineDateDTO.setSinaStockList(sinaStockList);

        return stockKLineDateDTO;
    }

    /**
     * 分页查询
     * @return
     */
    public Page<StockInfoDTO> selectStockInfoDTO(Long pageNum, Long pageSize, String stockName, String orderByColumn, String isAsc) {

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
        stockInfoDTO = StockDataUtil.construct(stockInfoDTO);
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
        stockInfoDTO = StockDataUtil.construct(stockInfoDTO);
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
                    StockInfoDTO construct = StockDataUtil.construct(stockInfoDTO);
                    stockInfoDTOMapper.updateById(construct);
                }
        );

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
        List<QtStock> stockInfoRealTime = StockDataUtil.getStockInfoRealTime(stockSymbol);
        return stockInfoRealTime;
    }

    public boolean saveStockChange() {
        List<StockChange> stockChanges = StockDataUtil.stockChangesEastMoney(new int[0]);
        return stockChangeMapper.saveOrUpdateBatch(stockChanges);
    }


    public List<String> getStockChange() {
        List<StockChange> stockChanges = StockDataUtil.stockChangesEastMoney(new int[0]);

        // 过滤火箭发射的代码
        List<String> HJFS = stockChanges.stream().filter(tmp -> tmp.getChangeType() == "火箭发射").map(tmp -> tmp.getStockName()).collect(Collectors.toList());


        // 过滤大笔买入的超过三次的股票名称
        Map<String, Long> DBMR = stockChanges.stream().filter(tmp -> tmp.getChangeType() == "大笔买入")
                .filter(tmp -> Integer.parseInt(tmp.getInfo().split(",")[0]) > 1000000)
                .collect(Collectors.groupingBy(stockChange -> stockChange.getStockName(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> result = new ArrayList<>();

        // 打印符合条件的分组统计
        DBMR.forEach((stockName, count) -> {
            if (HJFS.contains(stockName)) {
                result.add(stockName);
                System.out.println("Stock Names: " + stockName + " and count : " + count);
            }

            }
        );

        return result;

    }

    public List<StockChange> getAllStockChange(int[] changeType) {
        List<StockChange> stockChanges = StockDataUtil.stockChangesEastMoney(changeType);

       return stockChanges;

    }
}
