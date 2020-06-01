package com.ruoyi.acad.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
    }
    
    /**
     * Description:时间排序,由近到远
     * CreateTime:2020年4月1日下午5:30:55
     * @param <T>
     * @param list
     * @return
     * @throws Exception
     */
    public static List<LocalDateTime> compareForDateUp(List<LocalDateTime> list) throws Exception  {
    	
		Collections.sort(list ,new Comparator<LocalDateTime>() {
			@Override
			public int compare(LocalDateTime o1, LocalDateTime o2) {
			if (o1.isBefore(o2)) {
	                return 1;
	            } else if (o1.isAfter(o2)) {
	                return -1;
	            } else {
	                return 0;
	            }
			}
		});
		
		return list;
    }
}
