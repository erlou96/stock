package com.binzaijun.stock.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import org.elasticsearch.search.DocValueFormat;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtil {

    public static Date formatDate(String dateTime) {
        if (dateTime.length() < 6) {
            dateTime = "0" + dateTime;
        }
        Date d = new DateTime();

        DateTime date = cn.hutool.core.date.DateUtil.date();
        DateTime result = cn.hutool.core.date.DateUtil.parse(dateTime, "HHmmss");
        result.setField(DateField.MONTH, date.month());
        result.setField(DateField.YEAR, date.year());
        result.setField(DateField.DAY_OF_MONTH, date.dayOfMonth());
        return result.toJdkDate();
    }


    public static void main(String[] args) {
        String tmp = "82413";
        Date d = new DateTime();
        System.out.println(d.getTime());
    }
}
