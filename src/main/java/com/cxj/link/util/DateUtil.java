package com.cxj.link.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
public class DateUtil {

    private final static Log log = LogFactory.getLog(DateUtil.class);

    public static String DATE = "yyyy-MM-dd";
    public static String DATE_TIME = "yyyy-MM-dd HH:mm";
    public static String TIME = "yyyy-MM-dd HH:mm:ss";

    public static String OTHER_DATE = "yyyy/MM/dd";
    public static String OTHER_TIME = "yyyy/MM/dd HH:mm:ss";

    public static String SAMPLE_DATE = "yyyyMMdd";
    public static String HOUR_TIME = "yyyyMMddHH";
    public static String SAMPLE_TIME = "yyyyMMddHHmmss";

    public static String HH_MM = "HH:mm";
    public static String HH_MM_SS = "HH:mm:ss";

    public final static int SECONDS_OF_MINITE = 60;
    public final static int SECONDS_OF_HOUR = 3600;
    public final static int MILLISECONDS_OF_MINITE = SECONDS_OF_MINITE * 1000;
    public final static int MILLISECONDS_OF_HOUR = SECONDS_OF_HOUR * 1000;
    //一天
    public final static long MILLISECONDS_OF_DAY = 24 * MILLISECONDS_OF_HOUR;
    public final static int SECONDS_OF_DAY = 24 * SECONDS_OF_HOUR;
    /**
     * 格式如下： yyyy-MM-dd HH:mm:ss
     */
    public final static int DATE_FORMAT_BY_TIME = 1;

    /**
     * 格式如下： yyyy-MM-dd
     */
    public final static int DATE_FORMAT_BY_DATE = 2;

    /**
     * 格式如下： yyyy/MM/dd HH:mm:ss
     */
    public final static int DATE_FORMAT_BY_OTHER_TIME = 3;
    /**
     * 格式如下： yyyy/MM/dd
     */
    public final static int DATE_FORMAT_BY_OTHER_DATE = 4;

    /**
     * 格式如下： yyyyMMddHHmmss
     */
    public final static int DATE_FORMAT_BY_SIAMPLE_TIME = 5;
    /**
     * 格式如下： yyyyMMdd
     */
    public final static int DATE_FORMAT_BY_SIAMPLE_DATE = 6;


    private final static SimpleDateFormat rssDate = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.US);
    private final static SimpleTimeZone aZone = new SimpleTimeZone(8, "GMT");

    static {
        rssDate.setTimeZone(aZone);
    }

    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(TIME);
    }

    public static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat(DATE_TIME);
    }

    public static SimpleDateFormat getOtherTimeFormat() {
        return new SimpleDateFormat(OTHER_TIME);
    }

    public static SimpleDateFormat getOtherDateFormat() {
        return new SimpleDateFormat(OTHER_DATE);
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(DATE);
    }

    public static SimpleDateFormat getSampleTimeFormat() {
        return new SimpleDateFormat(SAMPLE_TIME);
    }

    public static SimpleDateFormat getSampleDateFormat() {
        return new SimpleDateFormat(SAMPLE_DATE);
    }

    public static SimpleDateFormat getHourDateFormat() {
        return new SimpleDateFormat(HOUR_TIME);
    }

    public static SimpleDateFormat getHHmmFormat() {
        return new SimpleDateFormat(HH_MM);
    }

    /**
     * 格式成HH:mm
     *
     * @param date
     * @return
     */
    public static String formatHHmm(Date date) {
        return getHHmmFormat().format(date);
    }

    /**
     * 当前时间与参数时间差距
     * <=5分钟    => 刚刚
     * <60分钟    => X分钟前
     * <24小时    => X小时前
     * <8天       => X天前
     * 其他       => yyyy-MM-dd HH:mm
     *
     * @param dt
     * @return
     */
    public static String getShortTimeDesc(Date dt) {
        String desc = "";
        int minute = DateUtil.getDiffMinite(dt, new Date());
        int hour = DateUtil.getDiffHour(dt, new Date());
        int day = DateUtil.getDiffDays(dt, new Date());

        if (minute <= 5) {
            desc = "刚刚";
        } else if (minute < 60) {
            desc = minute + "分钟前";
        } else if (hour < 24) {
            desc = hour + "小时前";
        } else if (day > 0 && day < 8) {
            desc = day + "天前";
        } else {
            desc = getDateTimeFormat().format(dt);
        }
        return desc;
    }

    /**
     * 将日期对象转换为RSS用的字符串日期
     *
     * @param d
     * @return
     */
    public static String dateToRssDate(Date d) {
        return rssDate.format(d);
    }

    public static Date getNowMinuteDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getNowHourDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getHourDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date[] getPreDates(int days) {
        Date[] dates = new Date[days];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        dates[0] = c.getTime();
        for (int i = 1; i < days; i++) {
            c.add(Calendar.DAY_OF_YEAR, -1);
            dates[i] = c.getTime();
        }
        return dates;
    }

    public static Date getYesterday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    public static String hourTimeFormat(Date date) {
        return getHourDateFormat().format(date);
    }

    public static String[] formats(SimpleDateFormat sdf, Date[] dates) {
        String[] dateFormats = new String[dates.length];
        try {
            for (int i = 0; i < dates.length; i++) {
                dateFormats[i] = sdf.format(dates[i]);
            }
        } catch (Exception e) {
            log.error("formats " + dates[0], e);
        }
        return dateFormats;
    }

    public static String[] formats(SimpleDateFormat sdf, List<Date> dateList, int size) {
        if (dateList == null || dateList.size() > size) {
            log.error("formats error");
        }
        String[] dateFormats = new String[size];
        try {
            for (int i = 0; i < dateList.size(); i++) {
                dateFormats[i] = sdf.format(dateList.get(i));
            }
        } catch (Exception e) {
            log.error("formats " + dateList.get(0), e);
        }
        return dateFormats;
    }

    /**
     * 将RSS日期字符串转换为日期对象
     *
     * @param
     * @return
     */
    public static Date rssDateToDate(String rd) {
        boolean ret = false;
        try {
            Date d = rssDate.parse(rd);
            ret = true;
            return d;
        } catch (Exception e) {
            log.error("rssDateToDate date " + rd, e);
        } finally {
            if (!ret) {
                log.error("rssDateToDate date " + rd);
            }
        }
        return null;
    }


    /**
     * 将时间对象转换为yyyyMMddHHmmss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String sampleTimeFormat(Date date) {
        return getSampleTimeFormat().format(date);
    }

    /**
     * 将时间对象转换为yyyyMMdd格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String sampleDateFormat(Date date) {
        return getSampleDateFormat().format(date);
    }


    /**
     * 将时间对象转换为yyyy/MM/dd HH:mm:ss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String otherTimeFormat(Date date) {
        return getOtherTimeFormat().format(date);
    }

    /**
     * 将时间对象转换为yyyy-mm-dd HH:mm:ss格式用的字符串时间
     *
     * @param date
     * @return
     */
    public static String timeFormat(Date date) {
        return getTimeFormat().format(date);
    }

    /**
     * 将日期对象转换为yyyy-mm-dd 格式用的字符串日期
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        return getDateFormat().format(date);
    }

    /**
     * 将日期对象转换为yyyy/mm/dd 格式用的字符串日期
     *
     * @param date
     * @return
     */
    public static String otherDateFormat(Date date) {
        return getOtherDateFormat().format(date);
    }

    /**
     * 将格式为yyyy-mm-dd HH:mm:ss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseTime(String date) {
        boolean ret = false;
        try {
            Date d = getTimeFormat().parse(date);
            ret = true;
            return d;
        } catch (Exception e) {
            log.error("parseTime date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseTime date " + date);
            }
        }
        return null;
    }

    /**
     * 将格式为yyyyMMddHHmmss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseSampleTime(String date) {
        boolean ret = false;
        try {
            Date d = getSampleTimeFormat().parse(date);
            ret = true;
            return d;
        } catch (Exception e) {
            log.error("parseSampleTime date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseSampleTime date " + date);
            }
        }
        return null;
    }

    /**
     * 将格式为yyyyMMdd字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseSampleDate(String date) {
        boolean ret = false;
        try {
            Date d = getSampleDateFormat().parse(date);
            ret = true;
            return d;
        } catch (Exception e) {
            log.error("parseSampleDate date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseSampleDate date " + date);
            }
        }
        return null;
    }

    /**
     * 将格式为yyyy/mm/dd HH:mm:ss字符串时间转换成时间对象
     *
     * @param date
     * @return
     */
    public static Date parseOtherTime(String date) {
        boolean ret = false;
        try {
            Date d = getOtherTimeFormat().parse(date);
            ret = true;
            return d;
        } catch (ParseException e) {
            log.error("parseOtherTime date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseOtherTime date " + date);
            }
        }
        return null;
    }

    /**
     * 将格式为yyyy-mm-dd字符串日期转换成日期对象
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        boolean ret = false;
        try {
            Date d = getDateFormat().parse(date);
            ret = true;
            return d;
        } catch (Exception e) {
            log.error("parseDate date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseDate date " + date);
            }
        }
        return null;
    }

    /**
     * 将格式为yyyy/mm/dd字符串日期转换成日期对象
     *
     * @param date
     * @return
     */
    public static Date parseOtherDate(String date) {
        boolean ret = false;
        try {
            Date d = getOtherDateFormat().parse(date);
            return d;
        } catch (ParseException e) {
            log.error("parseOtherDate date " + date, e);
        } finally {
            if (!ret) {
                log.error("parseOtherDate date " + date);
            }
        }
        return null;
    }

    /**
     * 获取当前系统时间yyyy/mm/dd HH:mm:ss格式表示的字符串
     *
     * @return
     */
    public static String getOtherNowTime() {
        return otherTimeFormat(new Date());
    }

    /**
     * 获取当前系统时间yyyy-mm-dd HH:mm:ss格式表示的字符串
     *
     * @return
     */
    public static String getNowTime() {
        return timeFormat(new Date());
    }

    /**
     * 获取当前系统时期yyyy-mm-dd格式表示的字符串
     *
     * @return
     */
    public static String getNowDate() {
        return dateFormat(new Date());
    }


    /**
     * 当前秒数
     *
     * @return
     */
    public static long getNowSec() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 当前毫秒数
     *
     * @return
     */
    public static long getNowMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 当前时间
     *
     * @return(Timestamp)
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 毫秒数转Date
     *
     * @param timeMillis 毫秒数
     * @return
     */
    public static Date timeMillisToDate(long timeMillis) {
        return new Date(timeMillis);
    }

    /**
     * 秒数转Date
     *
     * @param timeSeconds 秒数
     * @return
     */
    public static Date timeSecondsToDate(long timeSeconds) {
        return timeMillisToDate(timeSeconds * 1000L);
    }

    /**
     * 毫秒数转Timestamp
     *
     * @param timeMillis 毫秒数
     * @return
     */
    public static Timestamp timeMillisToTimestamp(long timeMillis) {
        return new Timestamp(timeMillis);
    }

    /**
     * 秒数转Timestamp
     *
     * @param timeSeconds 秒数
     * @return
     */
    public static Timestamp timeSecondsToTimestamp(long timeSeconds) {
        return timeMillisToTimestamp(timeSeconds * 1000L);
    }

    /**
     * 当前时间
     *
     * @return(Date)
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 获取当前系统时期yyyy-mm-dd格式表示的字符串
     *
     * @return
     */
    public static Date getDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date[] getDates() {
        Date[] dates = new Date[2];
        Calendar c = Calendar.getInstance();
        dates[0] = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        dates[1] = c.getTime();
        return dates;
    }


    public static Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static Date preDate(Date date, int subDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, -subDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取之前的几天的日期对象
     *
     * @param subDay
     * @return
     */
    public static Date preDate(int subDay) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -subDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();

    }

    /**
     * 获取当天上个月的日期
     *
     * @return
     */
    public static Date preMonthDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date preWeekDate() {
        return preDate(6);
    }


    /**
     * 获取日期为星期几
     *
     * @param date
     * @return 1-星期一 2-星期二 ...7-星期日
     */
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK);
        return week == 1 ? 7 : (week - 1);
    }

    /**
     * 获取当前星期几
     *
     * @return 1-星期一 2-星期二 ...7-星期日
     */
    public static int getWeek() {
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        return week == 1 ? 7 : (week - 1);
    }

    /**
     * time是否大于当前小时数
     *
     * @param hourOfDay
     * @param time
     * @return
     */
    public static boolean isGreaterThan(int hourOfDay, Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return time.getTime() >= c.getTimeInMillis();
    }

    /**
     * 两个时间相差天数(指定小时数)
     *
     * @param beginDate
     * @param endDate
     * @param hourOfDay
     * @return
     */
    public static int getDiffDaysByHour(Date beginDate, Date endDate, int hourOfDay) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);
        begin.set(Calendar.HOUR_OF_DAY, hourOfDay);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        long bt = begin.getTimeInMillis();
        begin.set(Calendar.HOUR_OF_DAY, 0);
        //说明上次超过此小时
        if (beginDate.getTime() >= bt) {
            begin.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        long et = end.getTimeInMillis();
        //说明本次超过此小时
        end.set(Calendar.HOUR_OF_DAY, 0);
        if (endDate.getTime() >= et) {
            end.add(Calendar.DAY_OF_YEAR, 1);
        }
        return (int) ((end.getTimeInMillis() - begin.getTimeInMillis()) / MILLISECONDS_OF_DAY);
    }

    /**
     * 两个时间相差天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        return (int) ((end.getTimeInMillis() - begin.getTimeInMillis()) / MILLISECONDS_OF_DAY);
    }


    /**
     * 将HH:mm时间格式转化为分钟
     *
     * @param time
     * @return
     */
    public static int parseTime2Int(String time) {
        String[] hm = time.split(":");
        return Integer.parseInt(hm[0]) * 60 + Integer.parseInt(hm[1]);
    }

    /**
     * 获得 大于time的最近的5分钟的倍数的时间
     *
     * @return
     */
    public static Date get5Multiple(long time) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int min = c.get(Calendar.MINUTE);
        min = min + (5 - min % 5);
        c.set(Calendar.MINUTE, min);
        return c.getTime();
    }

    /**
     * 获得两个时间相差小时数
     *
     * @param begin
     * @param end
     * @return
     */
    public static int getDiffHour(Date begin, Date end) {
        Calendar c0 = Calendar.getInstance();
        c0.setTime(begin);
        c0.set(Calendar.MINUTE, 0);
        c0.set(Calendar.SECOND, 0);
        c0.set(Calendar.MILLISECOND, 0);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(end);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        return (int) ((c1.getTimeInMillis() - c0.getTimeInMillis()) / MILLISECONDS_OF_HOUR);
    }

    /**
     * 获得两个时间相差分钟
     *
     * @param begin
     * @param end
     * @return
     */
    public static int getDiffMinite(Date begin, Date end) {
        Calendar c0 = Calendar.getInstance();
        c0.setTime(begin);
        c0.set(Calendar.SECOND, 0);
        c0.set(Calendar.MILLISECOND, 0);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(end);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        return (int) ((c1.getTimeInMillis() - c0.getTimeInMillis()) / MILLISECONDS_OF_MINITE);
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean isSameDay(Date beginDate, Date endDate) {
        return getDiffDays(beginDate, endDate) == 0;
    }

    /**
     * 判断两个日期是否同一周
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean isSameWeek(Date beginDate, Date endDate) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        //换算beginDate的周一时间
        int beginDayOfWeek = begin.get(Calendar.DAY_OF_WEEK);
        if (beginDayOfWeek == 1) {
            begin.add(Calendar.DAY_OF_YEAR, -6);
        } else if (beginDayOfWeek > 2) {
            begin.add(Calendar.DAY_OF_YEAR, 2 - beginDayOfWeek);
        }

        //换算endDate的周一时间
        int endDayOfWeek = end.get(Calendar.DAY_OF_WEEK);
        if (endDayOfWeek == 1) {
            end.add(Calendar.DAY_OF_YEAR, -6);
        } else if (endDayOfWeek > 2) {
            end.add(Calendar.DAY_OF_YEAR, 2 - endDayOfWeek);
        }
        return ((end.get(Calendar.YEAR) == begin.get(Calendar.YEAR)) && (end.get(Calendar.DAY_OF_YEAR) == begin.get(Calendar.DAY_OF_YEAR)));
    }

    public static boolean isInTime(Date time, Date begin, Date end) {
        long mill = time.getTime();
        return begin.getTime() < mill && mill < end.getTime();
    }

    /**
     * 判断某时间是否处在某个时间段内
     *
     * @param time  格式  hh:MM
     * @param begin 格式  hh:MM
     * @param end   格式  hh:MM
     * @return
     */
    public static boolean isInTime(String time, String begin, String end) {
        try {
            String[] timeArr = time.split(":");
            if (timeArr.length != 2) {
                return false;
            }
            byte hour = Byte.parseByte(timeArr[0]);
            byte min = Byte.parseByte(timeArr[1]);
            String[] beginArr = begin.split(":");
            if (beginArr.length != 2) {
                return false;
            }
            byte bHour = Byte.parseByte(beginArr[0]);
            byte bMin = Byte.parseByte(beginArr[1]);
            String[] endArr = end.split(":");
            if (endArr.length != 2) {
                return false;
            }
            byte eHour = Byte.parseByte(endArr[0]);
            byte eMin = Byte.parseByte(endArr[1]);
            return isInTime(hour, min, bHour, bMin, eHour, eMin);
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 判断 time 是否是在comp的时间区间内
     *
     * @param time
     * @param comp
     * @param bHour
     * @param bMin
     * @param eHour
     * @param eMin
     * @return
     */
    public static boolean isInTime(Date time, Date comp, int bHour, int bMin, int eHour, int eMin) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int d0 = c.get(Calendar.DAY_OF_YEAR);
        Calendar c1 = Calendar.getInstance();
        c.setTime(comp);
        int d1 = c1.get(Calendar.DAY_OF_YEAR);
        if (d0 != d1) {
            return false;
        }
        byte hour = (byte) c.get(Calendar.HOUR_OF_DAY);
        byte min = (byte) c.get(Calendar.MINUTE);
        return isInTime(hour, min, bHour, bMin, eHour, eMin);
    }

    /**
     * 给定时间是否在时间段内
     *
     * @param time  给定的时间
     * @param bHour 开始时间的小时
     * @param bMin  开始时间的分钟
     * @param eHour 结束时间的小时
     * @param eMin  结束时间的分钟
     * @return
     */
    public static boolean isInTime(Date time, int bHour, int bMin, int eHour, int eMin) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        byte hour = (byte) c.get(Calendar.HOUR_OF_DAY);
        byte min = (byte) c.get(Calendar.MINUTE);
        return isInTime(hour, min, bHour, bMin, eHour, eMin);
    }

    public static boolean isInTime(int hour, int min, int bHour, int bMin, int eHour, int eMin) {
        int time = hour * 60 + min;
        int begin = bHour * 60 + bMin;
        int end = eHour * 60 + eMin;
        return begin <= time && time <= end;
    }

    /**
     * 给定时间在时间段前内后状态（-1前  0内 1后）
     *
     * @param
     * @param bHour
     * @param bMin
     * @param eHour
     * @param eMin
     * @return
     */
    public static int getInTimeStatus(Date date, int bHour, int bMin, int eHour, int eMin) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        byte hour = (byte) c.get(Calendar.HOUR_OF_DAY);
        byte min = (byte) c.get(Calendar.MINUTE);
        int time = hour * 60 + min;
        int begin = bHour * 60 + bMin;
        int end = eHour * 60 + eMin;
        if (time < begin) {
            return -1;
        } else if (time > end) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 将 hh:mm-hh:mm格式的字符串转化为长度为4的字节数组
     *
     * @param time
     * @return
     */
    public static byte[] parseTime2Array(String time) {
        String[] fArr = time.split("\\-");
        String[] fArr0 = fArr[0].split(":");
        String[] fArr1 = fArr[1].split(":");
        return new byte[]{Byte.parseByte(fArr0[0]),
                Byte.parseByte(fArr0[1]),
                Byte.parseByte(fArr1[0]),
                Byte.parseByte(fArr1[1]),};
    }

    /**
     * 将date 向前或者向后移动几天
     *
     * @param date      指定的时间
     * @param dayNum    移动的天数
     * @param clearTime true-清除时分秒毫秒,false-不清除
     * @return
     */
    public static Date moveDate(Date date, int dayNum, boolean clearTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int curDay = c.get(Calendar.DAY_OF_YEAR);
        c.set(Calendar.DAY_OF_YEAR, curDay + dayNum);
        if (clearTime) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        }
        return c.getTime();
    }

    /**
     * 根据小时和分获得当天时间
     *
     * @param hour
     * @param minute
     * @return
     */
    public static Date getDateByHm(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 根据小时和分获得前一天时间
     *
     * @param hour
     * @param minute
     * @return
     */
    public static Date getPreDateByHm(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    /**
     * 根据小时和分获得后一天时间
     *
     * @param hour
     * @param minute
     * @return
     */
    public static Date getNextDateByHm(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    /**
     * 将date 向前或者向后移动几小时
     *
     * @param date      指定的时间
     * @param housrNum  移动的小时数
     * @param clearTime true-清除分秒毫秒,false-不清除
     * @return
     */
    public static Date moveHour(Date date, int housrNum, boolean clearTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        c.set(Calendar.HOUR_OF_DAY, curHour + housrNum);
        if (clearTime) {
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        }
        return c.getTime();
    }


    /**
     * 将date 向前或者向后移动几分钟
     *
     * @param date      指定的时间
     * @param minitNum  移动的分钟数
     * @param clearTime true-清除秒毫秒,false-不清除
     * @return
     */
    public static Date moveMinit(Date date, int minitNum, boolean clearTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int curHour = c.get(Calendar.MINUTE);
        c.set(Calendar.MINUTE, curHour + minitNum);
        if (clearTime) {
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        }
        return c.getTime();
    }


    /**
     * 将Date类型转换为字符串
     *
     * @param date 日期类型
     * @return 日期字符串
     */
    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date    日期类型
     * @param pattern 字符串格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "null";
        }
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date 字符串类型
     * @return 日期类型
     */
    public static Date format(String date) {
        return format(date, null);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date    字符串类型
     * @param pattern 格式
     * @return 日期类型
     */
    public static Date format(String date, String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null || date.equals("") || date.equals("null")) {
            return new Date();
        }
        Date d = null;
        try {
            d = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException pe) {
        }
        return d;
    }

    /**
     * @param date 指定的日期
     * @return Date
     * @Description 获取指定时间所在日期的开始时间
     */
    public static Date getStartTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param date 指定的日期
     * @return Date
     * @Description 获取指定时间所在日期的结束时间
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * @param date 需要转换的时间
     * @return 指定格式日期串
     * @Description 将对应时间转换为.net压入redis的时期格式
     */
    public static String transferNetDateString(Date date) {
        if (date == null) {
            return "";
        }
        return "/Date(" + date.getTime() + "-0000)/";
    }

    /**
     * @param netDate 需要转换的时间
     * @return 指定格式日期串
     * @Description 将对应时间转换为.net压入redis的时期格式
     */
    public static Date transferDateFromNetDate(String netDate) {
        if (StringUtils.isBlank(netDate)) {
            return null;
        }
        Date date = new Date();
        String time = netDate.substring(netDate.indexOf("Date(") + 5, netDate.indexOf("-000"));
        date.setTime(Long.parseLong(time.replaceAll("-", "")));
        return date;
    }

}
