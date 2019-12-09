package com.zous.catmaster.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String formatDate(long dateTime){
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, LocaleContextHolder.getLocale());;
        return dateFormat.format(new Date(dateTime));
    }
}
