package com.binzaijun.stock.common;

import java.util.HashMap;
import java.util.Map;

public enum StockChangeEnum {

    HJFS("火箭发射", 8201),
    KSFT("快速反弹", 8202),
    DBMR("大笔买入", 8193),
    FZTB("封涨停板", 4),
    KTTB("打开跌停板", 32),
    YDMP("有大买盘", 64),
    JJSZ("竞价上涨", 8207),
    KKMA5("高开5日线", 8209),
    XSQK("向上缺口", 8211),
    MA60XG("60日新高", 8213),
    MA60DFSZ("60日大幅上涨", 8215),
    JSXD("加速下跌", 8204),
    GTTS("高台跳水", 8203),
    DBMC("大笔卖出", 8194),
    FDTB("封跌停板", 8),
    KZTB("打开涨停板", 16),
    DMP("有大卖盘", 128),
    JJXD("竞价下跌", 8208),
    DKMA5("低开5日线", 8210),
    XXQK("向下缺口", 8212),
    MA60XD("60日新低", 8214),
    MA60DFXD("60日大幅下跌", 8216);



    private final String changeType;
    private final int value;

    // 构造函数
    StockChangeEnum(String changeType, int value) {
        this.changeType = changeType;
        this.value = value;
    }

    // 获取中文字符串
    public String getChangeType() {
        return changeType;
    }

    // 获取数字
    public int getValue() {
        return value;
    }

    // 创建一个静态的映射表，用于从数字获取枚举值
    private static final Map<Integer, StockChangeEnum> map = new HashMap<>();
    static {
        for (StockChangeEnum status : StockChangeEnum.values()) {
            map.put(status.value, status);
        }
    }

    // 根据数字获取枚举值
    public static StockChangeEnum fromValue(int value) {
        return map.get(value);
    }

    public static void main(String[] args) {
        StockChangeEnum stockChangeEnum = fromValue(8212);
        System.out.println(stockChangeEnum.changeType);
    }
}
