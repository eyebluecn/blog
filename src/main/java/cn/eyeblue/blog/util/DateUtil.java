package cn.eyeblue.blog.util;

import cn.eyeblue.blog.config.exception.UtilException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 时间通用转换
 */
public class DateUtil {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SLASH_DATE_FORMAT = "yyyy/MM/dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";


    //返回今天是星期几
    public static String dateToWeek(Date date) {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }

    public static String dateToWeek(Calendar calendar) {

        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");

        return dateFm.format(calendar.getTime());
    }


    //根据unix timestamp * 1000返回时间
    public static String timestamp2time(Long timestamp) {
        if (timestamp == null) return "未知";
        Date date = new Date(timestamp);
        SimpleDateFormat dateFm = new SimpleDateFormat("HH:mm");

        return dateFm.format(date);
    }

    //根据unix timestamp * 1000返回日期
    public static String timestamp2date(Long timestamp) {
        if (timestamp == null) return "未知";
        Date date = new Date(timestamp);
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");

        return dateFm.format(date);
    }

    //以下的四个方法都是方便人类阅读的方法。
    public static String humanDateTime(Date then) {
        if (then == null) {
            return null;
        }
        return humanDateWithFormat("yyyy-MM-dd HH:mm", then);
    }

    public static String humanDate(Date then) {
        if (then == null) {
            return null;
        }
        return humanDateWithFormat("yyyy-MM-dd", then);
    }


    public static String humanDateWithFormat(String formatKey, Date then) {
        if (then == null) {
            return null;
        }
        return humanStampWithFormat(formatKey, then.getTime() / 1000);

    }

    public static String humanStamp(long timeStamp) {
        return humanStampWithFormat("yyyy-MM-dd HH:mm", timeStamp);
    }

    public static String humanStampWithFormat(String formatKey, long timeStamp) {
        SimpleDateFormat tempDate = new SimpleDateFormat(formatKey);
        String res = null;
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long interval = currentTimestamp - timeStamp;
        if (interval >= 0) {
            if (interval < 60) {
                res = "刚刚";
            } else if (interval > 60 && interval < 3600) {
                res = (long) Math.ceil(interval / 60) + "分钟前";
            } else if (interval > 3600 && interval < (3600 * 6)) {
                long hour = (long) (Math.floor(interval / 3600));

                long minute = (long) (Math.ceil((interval % 3600) / 60));

                res = hour + "小时" + minute + "分钟前";


            } else {

                res = tempDate.format(new Date(timeStamp * 1000));

            }
        } else {
            interval = -interval;
            if (interval < 60) {
                res = interval + "秒后";
            } else if (interval > 60 && interval < 3600) {
                res = (long) Math.ceil(interval / 60) + "分钟后";
            } else if (interval > 3600 && interval < (3600 * 6)) {
                long hour = (long) (Math.floor(interval / 3600));

                long minute = (long) (Math.ceil((interval % 3600) / 60));

                res = hour + "小时" + minute + "分钟后";
            } else {
                res = tempDate.format(new Date(timeStamp * 1000));
            }
        }
        return res;
    }


    //获取今天 0:00的时间。
    public static Date getTodayStartDate() {

        return getStartDate(new Date());
    }

    //获取今天 23:59的时间
    public static Date getTodayEndDate() {

        return getEndDate(new Date());
    }


    //获取某天 0:00的时间。
    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    //获取某天 23:59的时间
    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    public static Date convertStringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {

            return formatter.parse(dateString);
        } catch (ParseException e) {


            throw new UtilException("无法转换");
        }
    }

    public static String convertDateToString(Date date) {

        return convertDateToString(date, DEFAULT_FORMAT);

    }

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat tempDate = new SimpleDateFormat(format);

        return tempDate.format(date);

    }

    //拼合时间，成为这种格式：2016-01-23@2016-02-12@2016-03-23
    public static String combineDates(List<Date> dates) {
        List<String> dateStrings = dates.stream().map(date -> DateUtil.convertDateToString(date, "yyyy-MM-dd")).collect(Collectors.toList());

        return String.join("@", dateStrings);

    }

    //拼合时间，成为这种格式: 01-23,02-12,03-23
    public static String combineDatesWithComma(List<Date> dates) {
        List<String> dateStrings = dates.stream().map(date -> DateUtil.convertDateToString(date, "MM-dd")).collect(Collectors.toList());

        return String.join(",", dateStrings);
    }


    //两天相差的天数
    public static int dayInterval(Date date1, Date date2) {
        return Math.round(date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
    }

    //两天相差的周数
    public static int weekInterval(Date date1, Date date2) {
        return Math.round(date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24 * 7);
    }
}
