package com.binzaijun.stock.util;

import com.binzaijun.stock.common.ExchangeEnum;
import com.binzaijun.stock.constant.Constants;
import com.binzaijun.stock.domain.QtStock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StockStringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StockStringUtil.class);

//0: 交易所
//1: 名字
//2: 代码
//3: 当前价格
//4: 涨跌
//5: 涨跌%
//6: 成交量（手）
//7: 成交额（万）
//8:
//9: 总市值
// "v_s_sh600519="1~贵州茅台~600519~1642.20~-0.86~-0.05~10934~179657~~20629.28~GP-A";"
    public static QtStock strFormat(String str) {
        if (StringUtils.isBlank(str)) {
            logger.warn("{}, String is empty or contains only white spaces", str);
            return null;
        } else {
            logger.info("{}, String is not empty", str);
        }
        String[] stock = str.split(Constants.QUOTES);
        str = stock[1];
        stock = str.split(Constants.TILDE);
        int exchange = Integer.parseInt(stock[0]);
        String stockName = stock[1];
        String stockSymbol = ExchangeEnum.fromValue(exchange) + stock[2];
        double currentPrice = Double.parseDouble(stock[3]);
        double change = Double.parseDouble(stock[4]);
        double changePercentage = Double.parseDouble(stock[5]);
        long volume = Long.parseLong(stock[6]);
        double turnover = Double.parseDouble(stock[7]);
        double marketValue = Double.parseDouble(stock[9]);
        QtStock qtStock = new QtStock(stockSymbol, stockName, exchange, currentPrice, change, changePercentage, volume, turnover, marketValue);
        return qtStock;
    }

    public static String formatUrl(List<String> stringList) {

        List<String> s_ = stringList.stream().map(str -> {
            StringBuilder stringBuilder = new StringBuilder("s_");
            stringBuilder.append(str);
            String prefix = stringBuilder.toString();
            return prefix;
        }).collect(Collectors.toList());

        String result = String.join(Constants.COMMA, s_);

        return result;
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("sh_600519", "sh_601949");
        String s = formatUrl(strings);
        logger.info(s);
        String str = "v_s_sh600519=\"1~贵州茅台~600519~1640.25~-2.81~-0.17~14158~232535~~20604.78~GP-A\"";
        strFormat(str);
    }
}
