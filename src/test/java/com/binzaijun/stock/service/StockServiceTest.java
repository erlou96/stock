package com.binzaijun.stock.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binzaijun.stock.common.AjaxResult;
import com.binzaijun.stock.domain.StockInfo;
import com.binzaijun.stock.domain.StockInfoDTO;
import com.binzaijun.stock.domain.StockWatchList;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private WatchListService watchListService;
//    @Test
//    public void getOrderTest() throws Exception {
//        StockInfoDTO sh11111 = stockService.getOneStockInfoDTO("sh11111");
//        if (sh11111 != null) {
//            System.out.println(sh11111.getStockSymbol());
//        }else {
//            System.out.println("数据为空");
//        }
//    }

    @Test
    public void test() {
        AjaxResult ajaxResult = new AjaxResult();
        List<StockInfo> guohua = stockService.getStockInfoByES("zgcb");
        guohua.stream().forEach(System.out::println);
    }

    @Test
    public void test1() {
        AjaxResult ajaxResult = new AjaxResult();
        int i = stockService.delStockInfoDTO("zgcb");
        System.out.println(i);
    }

    @Test
    public void test2() {
        Page<StockInfoDTO> stockInfoDTO = stockService.getStockInfoDTO(1l, 10l, "", "consecutiveDaysLimitUp", "asc");


    }

    @Test
    public void test3() {
        List<StockInfoDTO> test = stockService.getStockInfoDTOByStockName("中国出版");

    }

}