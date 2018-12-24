package io.feeeei.circleseekbar.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType, Locale locale)
            throws ParseException {
        if (TextUtils.isEmpty(strTime)) {
            return 0;
        }
        Date date = stringToDate(strTime, formatType, locale); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType, Locale locale)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType, locale);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));//**TimeZone时区，加上这句话就解决啦**
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static String longToString(long time, String formatType, Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatType,locale);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));//**TimeZone时区，加上这句话就解决啦**
        String format = simpleDateFormat.format(time);
        return format;
    }
}
