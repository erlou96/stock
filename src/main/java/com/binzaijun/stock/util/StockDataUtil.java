package com.binzaijun.stock.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.binzaijun.stock.common.StockChangeEnum;
import com.binzaijun.stock.constant.Constants;
import com.binzaijun.stock.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 请求外部数据接口
 */
public class StockDataUtil {

    private static Logger log = LoggerFactory.getLogger(StockDataUtil.class);

    // 交易日长度
    public static final int DATALEN = 60;
    // 周期：60 为一个小时，一天则是 60 * 4
    public static final int SCALE = 240;
    // 均线：5日，10日，20日
    public static final String MA = "5,10,20";
    // 最大涨幅：默认10%
    public static BigDecimal percentage_10 = new BigDecimal("1.1");
    // 新浪数据--获取个股历史数据
    public static String historyStockDataOfSinaURL = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData";
    // 腾讯数据--获取个股实时价格
    public static String realTimeStockDataOfQtURL = "http://qt.gtimg.cn/q=";
    // 东方财富数据-获取股票异动信息
    public static String stockChangesDataOfEastMoney = "http://push2ex.eastmoney.com/getAllStockChanges";

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
                    uriComponents = UriComponentsBuilder.fromUriString(historyStockDataOfSinaURL)
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
            // 第二天涨停的收盘价,并且设置小数位，和是否向上取整。
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

    /**
     * 构建数据
     * @param stockInfoDTO
     * @return
     */
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
     * 获取实时行情，价格
     * @param
     */
    public static List<QtStock> getStockInfoRealTime(List<String> symbol) {

        String urlSuffix = StockStringUtil.formatUrl(symbol);
        String requestBody = null;
        try{
            RestTemplate restTemplate = new RestTemplate();
            UriComponents
                    uriComponents = UriComponentsBuilder.fromUriString(realTimeStockDataOfQtURL + urlSuffix)
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

    /**
     * 获取东方财富--股票异动信息
     */
    public static List<StockChange> stockChangesEastMoney(int[] changeType) {

        log.info("开始获取东方财富股票异动接口信息...");

        // 全部的异动类型
        int[] defaultChangeType = new int[]{8201,8202,8193,4,32,64,8207,8209,8211,8213,8215,8204,8203,8194,8,16,128,8208,8210,8212,8214,8216};

        // 当传参没有异动类型时，自动使用默认的异动类型
        if (changeType == null || changeType.length == 0) {
            changeType = defaultChangeType;
        }

        // 拼接异动类型，转为字符串，作为请求参数k-v
        String result = Arrays.stream(changeType)
                .mapToObj(String::valueOf)
                .reduce((x, y) -> x + "," + y)
                .orElse("");


        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (Khtml, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3676.400 QQBrowser/10.5.3738.400");
        headers.set("Content-Type", "application/json"); // 根据实际需要设置其他 Header

        // 构建请求 URL，并添加请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(stockChangesDataOfEastMoney)
                .queryParam("type", result)
                .queryParam("pageindex", "0")
                .queryParam("pagesize", "10000")
                .queryParam("ut", "7eea3edcaed734bea9cbfc24409ed989")
                .queryParam("dpt", "wzchanges");

        // 创建 HttpEntity 对象，将请求头封装到其中
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // 发送请求并获取响应
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(), // 构建的请求 URL
                HttpMethod.GET, // 请求方法
                requestEntity, // 请求实体，包含请求头
                String.class); // 响应的数据类型

        // 获取响应体
        String responseBody = responseEntity.getBody();

        JSONObject jsonObject = JSON.parseObject(responseBody);

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("allstock");

        List<StockChange> stockChangeList = jsonArray.toJavaList(StockChange.class);

        stockChangeList = stockChangeList.stream().filter(tmp -> {
            // 过滤包含"60" 和 "00" 开头的主板股票
           return (tmp.getStockSymbol().startsWith("60") || tmp.getStockSymbol().startsWith("00")) && !tmp.getStockName().startsWith("ST") && !tmp.getStockName().startsWith("*ST");
        }).map(tmp -> {
            // 更新时间字段
            Date date = DateUtil.formatDate(tmp.getTm());
            tmp.setId(tmp.getStockSymbol()  + Constants.HYPHEN + tmp.getType() + Constants.HYPHEN + date.getTime());
            tmp.setDate(date);
            // 更新异动类型
            tmp.setChangeType(StockChangeEnum.fromValue(tmp.getType()).getChangeType());
            return tmp;
        }).collect(Collectors.toList());

        log.info("获取获取股票异动信息结束，获取条数: {}", stockChangeList.size());

        return stockChangeList;
    }

    public static void main(String[] args) throws Exception{

        String url = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sh600678&scale=240&ma=5&datalen=30";

//        List<SinaStock> sh600519 = getRequest("sh600519");
//
//        StockKLineDateDTO stockKLineDateDTO = new StockKLineDateDTO();
//
//        stockKLineDateDTO.setStockName("sh600519");
//
//        stockKLineDateDTO.setSinaStockList(sh600519);
//
//        System.out.println(stockKLineDateDTO);

        List<StockChange> stockChanges = stockChangesEastMoney(null);

//        for (StockChange stockChange: stockChanges) {
//            log.info(stockChange.toString());
//        }

//        EsUtil.bulkEsData(stockChanges);

    }
}
