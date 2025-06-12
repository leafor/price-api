package com.example.priceapi.infrastructure.adapter.support.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {

    private DateUtils(){}
    public static String getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss").format(timestamp);
    }
}
