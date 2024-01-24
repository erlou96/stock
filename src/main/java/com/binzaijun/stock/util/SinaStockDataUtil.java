package com.binzaijun.stock.util;

import com.alibaba.fastjson2.JSON;
import com.binzaijun.stock.constant.Constants;
import com.binzaijun.stock.domain.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SinaStockDataUtil {

    // 30个交易日
    public static final int DATALEN = 60;
    // 周期：60 为一个小时，一天则是 60 * 4
    public static final int SCALE = 240;
    public static final String MA = "5,10,20";
    public static BigDecimal percentage_10 = new BigDecimal("1.1");
    public static final String URL = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData";
    public static final String REALTIME_URL = "http://qt.gtimg.cn/q=";

    /**
     * 获取sina stock api
     * @param symbol
     * @return
     */
    public static List<SinaStock> getRequest(String symbol) {
        String requestBody = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            UriComponents
                    uriComponents = UriComponentsBuilder.fromUriString(URL)
                    .queryParam(Constants.DATALEN,DATALEN)
                    .queryParam(Constants.SCALE, SCALE)
                    .queryParam(Constants.MA, MA)
                    .queryParam(Constants.SYMBOL, symbol)
                    .build()
                    .encode();
            URI uri = uriComponents.toUri();
            requestBody = restTemplate.getForEntity(uri,String.class).getBody();
            System.out.println(requestBody);
            // 获取的数据转化成list
            List<SinaStock> sinaStockList = JSON.parseArray(requestBody, SinaStock.class);
            return sinaStockList;
        }catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取股票最大连板天数
     * @return
     */
    public static int getConsecutiveDaysOfStockPriceLimitUp(BigDecimal[][] stocks) {
        int res = 0;
        int day = 0;
        int len = stocks.length;
        if (len <= 1) return res;
        for (int i = 1; i < stocks.length; i++) {
            // 昨天收盘价
            BigDecimal closePrice = stocks[i-1][3];
            BigDecimal nowClosePrice = closePrice.multiply(percentage_10);
            // 第二天涨停的收盘价
            closePrice = nowClosePrice.setScale(2,  RoundingMode.HALF_UP);

            // 今天涨停的收盘价与实际的收盘价做比较
            if (closePrice.compareTo(stocks[i][3]) == 0) {
                day++;
            }else {
                day = 0;
            }
            res = Math.max(res, day);
        }
        return res;
    }

    /**
     * 股票涨停天数
     * @param stocks
     * @return
     */
    public static int getDaysOfStockPriceLimitUp(BigDecimal[][] stocks) {
        int res = 0;
        int len = stocks.length;
        if (len <= 1) {
            return res;
        }
        for (int i = 1; i < len; i++) {
            BigDecimal closePrice = stocks[i-1][3];
            BigDecimal product = closePrice.multiply(percentage_10);
            closePrice = product.setScale(2,  RoundingMode.HALF_UP);

            if (closePrice.compareTo(stocks[i][3]) == 0) {
                res++;
            }
        }
        return res;
    }

    /**
     * 计算股价最高值，最低值，最高值日期，最低值日期
     */
    public static StockInfoDTO stockHighLowPriceInfo(List<SinaStock> stockList, StockInfoDTO stockInfoDTO) {
        // list 转数组
        BigDecimal[][] stocks = listToArray(stockList);
        int highDay = 0;
        int lowDay = 0;
        int len = stocks.length;
        BigDecimal highPrice = new BigDecimal("0");
        BigDecimal lowPrice = new BigDecimal("999999");

        for (int i = 0; i < len; i++) {
            if (stocks[i][0].compareTo(highPrice) > 0) {
                highPrice = stocks[i][0];
                highDay = i;
                lowPrice = stocks[i][1];
                lowDay = i;
            }else {
                if (stocks[i][1].compareTo(lowPrice) <= 0) {
                    lowDay = i;
                    lowPrice = stocks[i][1];
                }
            }
        }

//        StockInfoDTO stockInfoDTO = new StockInfoDTO();
        stockInfoDTO.setHighestPrice(highPrice);
        stockInfoDTO.setLowestPrice(lowPrice);
        stockInfoDTO.setHighestDate(stockList.get(highDay).getDay());
        stockInfoDTO.setLowestDate(stockList.get(lowDay).getDay());
        return stockInfoDTO;
    }

    /**
     * 计算 股票最高值和最低值的波动区间
     * @param highPrice
     * @param lowPrice
     * @return
     */
    public static double stockPriceVolatility(BigDecimal highPrice, BigDecimal lowPrice) {

        BigDecimal divideResult = lowPrice.divide(highPrice, 2, RoundingMode.HALF_UP);
        double res = divideResult.doubleValue();
        return res;
    }

    /**
     * list 转 数组
     * @param
     */
    public static BigDecimal[][] listToArray(List<SinaStock> stockList) {
        int size = stockList.size();

        BigDecimal[][] stocks = new BigDecimal[size][4];

        int i = 0;
        for (SinaStock stock : stockList) {
            stocks[i][0] = stock.getHigh();
            stocks[i][1] = stock.getLow();
            stocks[i][2] = stock.getOpen();
            stocks[i][3] = stock.getClose();
            i++;
        }
        return stocks;
    }

    public static StockInfoDTO construct(StockInfoDTO stockInfoDTO) {
        List<SinaStock> sinaStockList = getRequest(stockInfoDTO.getStockSymbol());
        stockInfoDTO = stockHighLowPriceInfo(sinaStockList, stockInfoDTO);
        BigDecimal[][] stocks = listToArray(sinaStockList);
        // 波动率
        double volatility = stockPriceVolatility(stockInfoDTO.getHighestPrice(), stockInfoDTO.getLowestPrice());
        // 涨停天数
        int day = getDaysOfStockPriceLimitUp(stocks);
        stockInfoDTO.setDaysLimitUp(day);
        // 连扳次数
        int consecutiveDays = getConsecutiveDaysOfStockPriceLimitUp(stocks);
        stockInfoDTO.setConsecutiveDaysLimitUp(consecutiveDays);
        stockInfoDTO.setVolatility(volatility);
        return stockInfoDTO;
    }

    /**
     * 获取实时行情
     * @param
     */
    public static List<QtStock> getStockInfoRealTime(List<String> symbol) {

        String urlSuffix = StockStringUtil.formatUrl(symbol);
        String requestBody = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            UriComponents
                    uriComponents = UriComponentsBuilder.fromUriString(REALTIME_URL + urlSuffix)
                    .build()
                    .encode();
            URI uri = uriComponents.toUri();
            requestBody = restTemplate.getForEntity(uri,String.class).getBody();
            // v_s_sh600519="1~贵州茅台~600519~1641.08~-2.91~-0.18~12645~207805~~20615.21~GP-A";

            // 按照 ; 分隔
            String[] stockRealTime = requestBody.split(Constants.SEMICOLON);

            List<QtStock> qtStockList = new ArrayList<>();

            for (String qtStockInfo : stockRealTime) {
                QtStock qtStock = StockStringUtil.strFormat(qtStockInfo);
                if(!Objects.isNull(qtStock)) {
                    qtStockList.add(qtStock);
                }
            }

            return qtStockList;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) {
       String url = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sh600678&scale=240&ma=5&datalen=30";

        List<SinaStock> sh600519 = getRequest("sh600519");

        StockKLineDateDTO stockKLineDateDTO = new StockKLineDateDTO();

        stockKLineDateDTO.setStockName("sh600519");

        stockKLineDateDTO.setSinaStockList(sh600519);

        System.out.println(stockKLineDateDTO);


    }
}
