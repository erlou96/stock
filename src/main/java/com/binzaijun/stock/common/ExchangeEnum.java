package com.binzaijun.stock.common;

public enum ExchangeEnum {
    // 1 表示上交所， 51表示深交所
    sh(1),
    sz(51);

    private final int value;

    ExchangeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ExchangeEnum fromValue(int value) {
        for (ExchangeEnum exchange : values()) {
            if (exchange.value == value) {
                return exchange;
            }
        }
        throw new IllegalArgumentException("Invalid value for Gender: " + value);
    }
}
