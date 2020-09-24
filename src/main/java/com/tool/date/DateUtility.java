package com.tool.date;

import com.config.path.ConfigParams;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class DateUtility {

    public static String getCurrentDate()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConfigParams.dateFormat);
        String today = now.format(formatter);
        return today;
    }

    public static String convertSQLDate(Date date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConfigParams.dateFormat);
        LocalDate localDateTime = date.toLocalDate();
        String dateFormated = localDateTime.format(formatter);
        return dateFormated;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    }
}
