package com.cxj.link.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * 进制转换
 *
 * @author caijian
 */
public class MathUtils {

    private static char[] charSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 将10进制转化为62进制
     *
     * @param number
     * @return
     */
    public static String _10_to_62(Long number) {
        Long rest = Math.abs(number);
        Deque<Character> deque = new ArrayDeque<>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            int i = Long.valueOf((rest - (rest / 62) * 62)).intValue();
            deque.push(charSet[i]);
            rest = rest / 62;
        }
        for (; !deque.isEmpty(); ) {
            result.append(deque.pop());
        }

        return result.toString();

    }
}
