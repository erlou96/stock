package com.binzaijun.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockApplication {
// 程序启动入口
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

}
