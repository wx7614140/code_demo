package org.febsteam.demos.jta.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author wx
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_DATE_PATTERN = "yyyyMMdd";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 格式化时间，格式为 yyyyMMddHHmmss
     *
     * @param localDateTime LocalDateTime
     * @return 格式化后的字符串
     */
    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param localDateTime LocalDateTime
     * @param format        格式
     * @return 格式化后的字符串
     */
    public static String formatFullTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 根据传入的格式，格式化时间
     *
     * @param date   Date
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化 CST类型的时间字符串
     *
     * @param date   CST类型的时间字符串
     * @param format 格式
     * @return 格式化后的字符串
     * @throws ParseException 异常
     */
    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return getDateFormat(usDate, format);
    }

    /**
     * 格式化 Instant
     *
     * @param instant Instant
     * @param format  格式
     * @return 格式化后的字符串
     */
    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 判断当前时间是否在指定时间范围
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 结果
     */
    public static boolean between(LocalTime from, LocalTime to) {
        LocalTime now = LocalTime.now();
        return now.isAfter(from) && now.isBefore(to);
    }

    /**
     * 返回当前时间的yyyy-MM-dd HH:mm:ss格式
     */
    public static String currentTimeFormatToString() {
        long nowTime = System.currentTimeMillis();
        DateFormat sdf = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN);
        String format = sdf.format(nowTime);
        return format;
    }

    /**
     * 返回当前时间的date格式
     */
    public static Date dateFormatToDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = timestamp;
        return date;
    }

    /**
     * 时间戳转为日期格式
     */
    public static Date timestampToDate(Long timstamp) {
        return new Date(timstamp);
    }

    /**
     * 日期转化为10位时间戳
     */
    public static Long dateToTimestamp(Date date) {

        return date.getTime()/1000;
    }

    /**
     * 获取当天开始时间
     * @return
     */
    public static Date getDayBegin(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);//0点
        cal.set(Calendar.MINUTE, 0);//0分
        cal.set(Calendar.SECOND, 0);//0秒
        cal.set(Calendar.MILLISECOND, 0);//0毫秒
        return cal.getTime();
    }

    /**
     * 两个日期相减得到的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if(beginDate==null||endDate==null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff=(endDate.getTime()-beginDate.getTime())/(1000*60*60*24);
        int days = new Long(diff).intValue();
        return days;
    }

    /**
     * 两个时间相减得到的秒数
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Long getDiffSeconds(Date beginTime, Date endTime) {
        if(beginTime==null && endTime==null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        if(endTime == null){
            endTime = new Date();
        }
        Long diff=(endTime.getTime()-beginTime.getTime())/(1000);
        return diff;
    }

    /**
     * 根据传入的格式化日期，格式化时间
     *
     * @param dateTime   Date
     * @return Date类型日期
     */
    public static Date stringFormatToDate(String dateTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_DATE_FORMAT, Locale.CHINA);
        return simpleDateFormat.parse(dateTime);
    }


    /**
     * 获取某时间进行加减 n 秒后的时间
     * @param dateTime
     * @param time (second)
     * @param addTime true：进行时间的相加，意为该时间之后的时间
     *                false : 该时间之前的时间
     * @return
     */
    public static String getDateTime(String dateTime,Integer time,Boolean addTime) throws ParseException {
        if(addTime){
            SimpleDateFormat formatter = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN);
            Date parse = formatter.parse(dateTime);
            String formatTime = formatter.format(parse.getTime() + time * 1000);
            return formatTime;
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN);
            Date parse = formatter.parse(dateTime);
            String formatTime = formatter.format(parse.getTime() - time * 1000);
            return formatTime;
        }
    }


}
