package com.binzaijun.stock.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Controller
public class Test {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "I am Jack";
    }

    public static void main(String[] args) {
        float a = 0.1f;
        float f = 0.2f;
        System.out.println(a + f == 0.3f);
        Double d = 10.04;
        for (int i = 0; i <= 5 ;i++) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedResult = decimalFormat.format(d + d * 0.1);
            d = Double.parseDouble(formattedResult);
            System.out.println(formattedResult);
        }
        d = 13.35;

        BigDecimal b = new BigDecimal("10.04");
        BigDecimal c = new BigDecimal("1.1");
        for (int i = 0; i <= 5 ;i++) {
            BigDecimal product = b.multiply(c);
            b = product.setScale(2,  RoundingMode.HALF_UP);
            System.out.println("Product: " + b);
        }



    }
}
