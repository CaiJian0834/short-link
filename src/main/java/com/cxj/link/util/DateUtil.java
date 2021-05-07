package com.cxj.link.util;

import java.util.*;

/**
 * 日期工具类
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
public class DateUtil {

    private DateUtil() {
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


}
